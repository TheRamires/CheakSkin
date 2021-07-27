package ru.skinallergic.checkskin.components.home.repositories

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.Api.FoodService
import ru.skinallergic.checkskin.Api.LpuService
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.Request
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicEntity
import ru.skinallergic.checkskin.components.healthdiary.remote.BaseResponse
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.home.data.LpuEntity
import ru.skinallergic.checkskin.components.home.data.LpuOneEntity
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.type.Either
import ru.skinallergic.checkskin.type.Failure
import ru.skinallergic.checkskin.utils.TOKENEXPIRED
import javax.inject.Inject

class LpuRepository  @Inject constructor(
        val service: LpuService,
        tokenService: TokenService,
        tokenModel: TokenModelImpls,
        val networkHandler: NetworkHandler,
        val toastyManager: ToastyManager
): BaseHealthyRepository(tokenService, tokenModel,networkHandler,toastyManager){

    fun getLpuList () : Observable<List<LpuEntity>>?{
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let {token ->
            service.allLpu(token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getLpuList() }
                        }
                    }
                    .map { it.data!! }


        }
    }
    fun getLpuFavorites () : Observable<List<LpuEntity>>?{
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let {token ->
            service.getFavorites(token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getLpuFavorites() }
                        }
                    }
                    .map { it.data!! }


        }
    }
    fun getOneLpu(id: Int) : Observable<LpuOneEntity>?{
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let {token ->
            service.oneLpu(id,token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getOneLpu(id) }
                        }
                    }
                    .map { it.data!! }
        }
    }

    override fun redact(date: String, data: WritingData, saved: MutableLiveData<Boolean>, progressBar: ObservableField<Boolean>) {}
    override fun getData(date: String, liveData: MutableLiveData<GettingData?>, splashScreenOn: ObservableField<Boolean>?) {}
}