package ru.skinallergic.checkskin.components.home.interactors

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.type.Either
import ru.skinallergic.checkskin.type.Failure

abstract class UseCase <Type, in Params> {

    var compositeDisposable= CompositeDisposable()

    abstract fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Observable<Either<Failure, Type>>) -> Unit = {}) {

        val result=Observable.just(run(params))
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())

        onResult(result)
    }

    fun unsubscribe() {
        compositeDisposable.dispose()
    }
}