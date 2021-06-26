package ru.skinallergic.checkskin.components.news;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.news.pagedLibrary.MySourceFactory;
import ru.skinallergic.checkskin.components.news.pojo.Datum;
import ru.skinallergic.checkskin.components.news.pojo.NewsEntity;
import ru.skinallergic.checkskin.components.news.pojo.OneNewsEntity;
import ru.skinallergic.checkskin.components.news.pojo.QueryParamNews;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsViewModel extends ViewModel {
    public ObservableField<Boolean> splashScreen=new ObservableField<>();
    public ObservableField<Boolean> progressBar=new ObservableField<>();
    public MutableLiveData<List<Datum>> newsListLive=new MutableLiveData<>();
    public MutableLiveData<List<Datum>> uploadNewsLive=new MutableLiveData<>();
    public MutableLiveData<OneNewsEntity> oneNewsLive=new MutableLiveData<>();
    private final NewsRepositoriy repositoriy;
    private final CompositeDisposable compositeDisposable=new CompositeDisposable();

    public LiveData<PagedList<Datum>> pagedListLiveData=new MutableLiveData<PagedList<Datum>>();

    private final int count=10;

    @Inject
    public NewsViewModel(NewsRepositoriy repositoriy){
        this.repositoriy=repositoriy;
    }

    public LiveData<PagedList<Datum>> getNewsList(String search, Boolean splashOn){
        QueryParamNews queryParam=new QueryParamNews(search,"",1,count);
        return getPagingList(queryParam, splashOn);
    }

    public void getNewsList(String search, int page){
        QueryParamNews queryParam=new QueryParamNews(search,"",page,count);
        getNewsList(queryParam,false);
    }
    public void getNewsList(String search, int page,boolean splashOn){
        QueryParamNews queryParam=new QueryParamNews(search,"",page,count);
        getNewsList(queryParam,splashOn);
    }

    public void getNewsList(int page, int count){
        QueryParamNews queryParam=new QueryParamNews("","",page,count);
        getNewsList(queryParam,false);
    }

    public void getNewsList(int page){
        QueryParamNews queryParam=new QueryParamNews("","",page,count);
        getNewsList(queryParam,false);
    }

 /*   public void getNewsList(String search, int page){
        QueryParamNews queryParam=new QueryParamNews(search,"",page,30);
        getNewsList(queryParam);
    }

    public void getNewsList(int page, int count){
        QueryParamNews queryParam=new QueryParamNews("","",page,count);
        getNewsList(queryParam);
    }

    public void getNewsList(int page){
        QueryParamNews queryParam=new QueryParamNews("","",page,30);
        getNewsList(queryParam);
    }
*/

    private LiveData<PagedList<Datum>> getPagingList(QueryParamNews queryParam, boolean splashOn){
        Loger.log("getNewsList");

        PagedList.Config config= new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(queryParam.getCount())
                .setPageSize(queryParam.getCount())
                .build();
        MySourceFactory sourceFactory = new MySourceFactory(
                repositoriy,
                queryParam,
                splashScreen,
                progressBar,
                splashOn
        );
        pagedListLiveData= new  LivePagedListBuilder(sourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
        return pagedListLiveData;
    }

    private void getNewsList(QueryParamNews queryParam, boolean splashOn){
                compositeDisposable.add(
                        repositoriy.getNewsList(queryParam)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                                /*.retryWhen((@NonNull Observable<Throwable> throwableObservable) -> {
                                    Loger.log("repeat getNewsList ☺");
                                    return throwableObservable.take(2).delay(1, TimeUnit.SECONDS);
                                    })*/
                                .doOnSubscribe((@NonNull Disposable disposable)->{
                                    if (splashOn){ splashScreen.set(true);}
                                    else progressBar.set(true);
                                })
                                .doOnComplete(()-> {
                                    if (splashOn){ splashScreen.set(false);}
                                    else progressBar.set(false);
                                })
                                .doOnError((@NonNull Throwable throwable) -> {
                                    if (splashOn){ splashScreen.set(false);}
                                    else progressBar.set(false);
                                })
                        .subscribe((NewsEntity response)-> {
                            List<Datum> list =response.getData();
                            Loger.log("getNewsList "+list.size());
                            newsListLive.setValue(list);

                        }, throwable ->{
                            Loger.log("getNewsList "+throwable);
                        })
        );
    }

    public void uploadNews(QueryParamNews queryParam){
        compositeDisposable.add(
                repositoriy.getNewsList(queryParam)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((NewsEntity response)-> {
                            List<Datum> list =response.getData();
                            Loger.log("getNewsList "+list.size());
                            uploadNewsLive.setValue(list);

                        }, throwable ->{
                            Loger.log("getNewsList "+throwable);
                        })
        );
    }

    public void getOneNews(String id){
                compositeDisposable.add(
                        repositoriy.getOneNews(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe((@NonNull Disposable disposable) -> {
                            splashScreen.set(true);
                        })
                        .doOnComplete(()-> {
                            splashScreen.set(false);
                        })
                        .subscribe(oneNews->{
                            oneNewsLive.setValue(oneNews);

                        }, throwable -> {
                            Loger.log("getOneNews "+throwable);
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}