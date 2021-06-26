package ru.skinallergic.checkskin.components.news.pagedLibrary

import android.util.Log
import androidx.databinding.ObservableField
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.news.NewsRepositoriy
import ru.skinallergic.checkskin.components.news.pojo.Datum
import ru.skinallergic.checkskin.components.news.pojo.QueryParamNews

class MySourceFactory (private var repository: NewsRepositoriy,
                        val queryParamNews: QueryParamNews,
                        var splashScreen: ObservableField<Boolean>,
                        var progressBar:ObservableField<Boolean>,
                        val splashOn: Boolean ) :
        DataSource.Factory<Int?, Datum?>() {
    val compositeDisposable = CompositeDisposable()

    override fun create(): DataSource<Int?, Datum?> {
        return MyPageDataSource(repository,queryParamNews, splashScreen, progressBar, splashOn)
    }

    /*
    fun getDate (){
        compositeDisposable.add(repository.getNewsList(queryParamNews: QueryParamNews)
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
                .subscribe({
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
                }, {
                    Loger.log("loadInitial " + it)
                    compositeDisposable.dispose()
                }))
    }    */
}