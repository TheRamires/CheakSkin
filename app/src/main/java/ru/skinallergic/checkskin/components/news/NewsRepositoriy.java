package ru.skinallergic.checkskin.components.news;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.skinallergic.checkskin.Api.ApiService;
import ru.skinallergic.checkskin.handlers.NetworkHandler;
import ru.skinallergic.checkskin.components.news.pojo.NewsEntity;
import ru.skinallergic.checkskin.components.news.pojo.OneNewsEntity;
import ru.skinallergic.checkskin.components.news.pojo.QueryParamNews;

import io.reactivex.Observable;
import javax.inject.Inject;

public class NewsRepositoriy {
    private ApiService apiService;
    private NetworkHandler networkHandler;

    @Inject
    public NewsRepositoriy (ApiService apiService, NetworkHandler networkHandler){
        this.apiService=apiService;
        this.networkHandler=networkHandler;
    }

    public Observable<NewsEntity> getNewsList ( QueryParamNews queryParam){
        networkHandler.check();
        return apiService.getNewList(
                queryParam.getWord(),
                queryParam.getKeywords(),
                queryParam.getPage(),
                queryParam.getCount());
    }

    public Observable<OneNewsEntity> getOneNews(String id){
        networkHandler.check();
        return apiService.getOneNews(id);
    }
}
