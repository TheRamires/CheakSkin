package ru.skinallergic.checkskin.components.healthdiary.repositories

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.Api.HealthyService
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.remote.BaseResponse
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.utils.TOKENEXPIRED
import javax.inject.Inject

open class HealthyDiaryRepository @Inject constructor(
        val service: HealthyService,
        tokenService: TokenService,
        tokenModel: TokenModelImpls,
        val networkHandler: NetworkHandler,
        val toastyManager: ToastyManager
): BaseHealthyRepository(tokenService, tokenModel,networkHandler,toastyManager) {

    override fun redact(date: String, data: WritingData, saved: MutableLiveData<Boolean>, progressBar : ObservableField<Boolean>) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let {
            compositeDisposable.add(service.redact(it, date, data).subscribeOn(Schedulers.io())
                    .doOnSubscribe { progressBar.set(true) }
                    .doOnComplete { progressBar.set(false) }
                    .doOnError { progressBar.set(false) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        saved.value = it.message == "Ok"
                    }, {
                        Loger.log("*************************2>>> ${it.message}")
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { redact(date, data, saved,progressBar) }
                        }
                    }))
        }
    }

    override fun getData(date: String, liveData: MutableLiveData<GettingData?>,splashScreenOn : ObservableField<Boolean>?) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let {
            compositeDisposable.add(service.getData(accesToken, date)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { splashScreenOn?.set(true) }
                    .doOnComplete { splashScreenOn?.set(false) }
                    .subscribe({
                        if (it.data==null){
                            liveData.value = null
                        } else {
                            val writingData = it.data
                            Loger.log("getData " + writingData)
                            liveData.value = writingData
                        }
                    }, {
                        Loger.log("*************************2>>> ${it.message}")
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getData(date, liveData,splashScreenOn) }
                        }
                    }))
        }
    }
    open fun date(date: String?): Observable<BaseResponse<GettingData?>>? {
        Loger.log("data start for repo")
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        if (accesToken==null && date==null){return null}
        return accesToken?.let{
            service.getData(accesToken, date!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError "+it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { date(date) }
                        }}

        }
    }
}