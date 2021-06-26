package ru.skinallergic.checkskin.components.healthdiary.repositories

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.Api.HealthyService
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
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

    override fun redact(date: String, data: WritingData, saved: MutableLiveData<Boolean>) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let {
            compositeDisposable.add(service.redact(it, date, data).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        saved.value = it.message == "Ok"
                    }, {
                        Loger.log("*************************2>>> ${it.message}")
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { redact(date, data, saved) }
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
}