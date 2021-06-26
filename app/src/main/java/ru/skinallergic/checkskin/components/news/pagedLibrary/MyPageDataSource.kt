package ru.skinallergic.checkskin.components.news.pagedLibrary

import android.util.Log
import androidx.databinding.ObservableField
import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.news.NewsRepositoriy
import ru.skinallergic.checkskin.components.news.pojo.Datum
import ru.skinallergic.checkskin.components.news.pojo.NewsEntity
import ru.skinallergic.checkskin.components.news.pojo.QueryParamNews

class MyPageDataSource(val repository: NewsRepositoriy,
                       var queryParamNews: QueryParamNews,
                       var splashScreen: ObservableField<Boolean>,
                       var progressBar: ObservableField<Boolean>,
                       val splashOn: Boolean
): PageKeyedDataSource<Int, Datum>() {
    val compositeDisposable = CompositeDisposable()
    private var page:Int=queryParamNews.page;

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Datum>) {

        queryParamNews.page  // =1
        queryParamNews.count=params.requestedLoadSize

        compositeDisposable.add(request(queryParamNews,splashOn)
                .subscribe{
                    val list: List<Datum> = it.data!!

                    var nextPage: Int? = null;
                    var previosPage: Int? = null;
                    val pagination = it.meta?.pagination
                    if (pagination?.hasNext == true) {
                        nextPage = page + 1
                    }
                    if (pagination?.hasPrev == true) {
                        previosPage = page - 1
                    }
                    Log.d("myLog", "\n loadInitial, page" + page +
                            " \n params.requestedLoadSize " + params.requestedLoadSize + ", list.size" + list.size)
                    callback.onResult(list, previosPage, nextPage)
                })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Datum>) {

        queryParamNews.page=params.key
        queryParamNews.count=params.requestedLoadSize

        compositeDisposable.add(request(queryParamNews,false)
                .subscribe({
                    val list: List<Datum> = it.data!!

                    var previosPage: Int? = null;
                    val pagination = it.meta?.pagination
                    if (pagination?.hasPrev == true) {
                        previosPage = params.key - 1
                    }
                    Log.d("myLog", "\n loadBefore, page" + params.key +
                            " \n params.requestedLoadSize " + params.requestedLoadSize + ", list.size" + list.size)
                    callback.onResult(list, previosPage)

                }, {
                    Loger.log("loadBefore " + it)
                    compositeDisposable.dispose()
                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Datum>) {
        queryParamNews.page=params.key
        queryParamNews.count=params.requestedLoadSize

        compositeDisposable.add(request(queryParamNews,false)
                .subscribe{
                    val list: List<Datum> = it.data!!

                    var previosPage: Int? = null;
                    val pagination = it.meta?.pagination
                    if (pagination?.hasPrev == true) {
                        previosPage = params.key + 1
                    }
                    Log.d("myLog", "\n loadBefore, page" + params.key +
                            " \n params.requestedLoadSize " + params.requestedLoadSize + ", list.size" + list.size)
                    callback.onResult(list, previosPage)
                }
        )
    }

    private fun request(queryParamNews: QueryParamNews,
                        splashOn: Boolean
    ): Observable<NewsEntity> {
        return repository.getNewsList(queryParamNews)
                .doOnSubscribe {
                    if (splashOn){
                        splashScreen.set(true)
                    }else progressBar.set(true)
                }
                .doOnComplete {
                    splashScreen.set(false)
                    progressBar.set(false)
                    compositeDisposable.dispose()
                }
                .doOnError {
                    Loger.log("loadAfter " + it)
                    compositeDisposable.dispose()
                }
    }
}