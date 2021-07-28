package ru.skinallergic.checkskin.components.home.repositories

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.Api.DocService
import ru.skinallergic.checkskin.Api.HomeService
import ru.skinallergic.checkskin.Api.LpuService
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.Request
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.home.data.DocEntity
import ru.skinallergic.checkskin.components.home.data.ReviewWriter
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.utils.TOKENEXPIRED
import javax.inject.Inject

class DocumentRepository @Inject constructor(
        val service: DocService,
        tokenService: TokenService,
        tokenModel: TokenModelImpls,
        val networkHandler: NetworkHandler,
        val toastyManager: ToastyManager
): BaseHealthyRepository(tokenService, tokenModel,networkHandler,toastyManager){

    fun getDocs(): Observable<List<DocEntity>>? {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
         return accesToken?.let {token ->
            service.getDocs()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getDocs() }
                        }
                    }
                    .map { it.data!! }

        }
    }
    override fun redact(date: String, data: WritingData, saved: MutableLiveData<Boolean>, progressBar: ObservableField<Boolean>) {}

    override fun getData(date: String, liveData: MutableLiveData<GettingData?>, splashScreenOn: ObservableField<Boolean>?) {}

}