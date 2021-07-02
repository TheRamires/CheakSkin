package ru.skinallergic.checkskin.components.healthdiary.repositories

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.entrance.pojo.RefreshTokenResponse
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.utils.REFRESHTOKEN_EXPIRED
import ru.skinallergic.checkskin.utils.REFRESH_TOKEN

abstract class BaseHealthyRepository (
        val tokenService_: TokenService,
        val tokenModel_: TokenModelImpls,
        val networkHandler_: NetworkHandler,
        val toastyManager_: ToastyManager) {

    lateinit var compositeDisposable : CompositeDisposable
    lateinit var expiredRefreshToken :MutableLiveData<Boolean>

    abstract fun redact(date:String, data: WritingData, saved : MutableLiveData<Boolean>,progressBar : ObservableField<Boolean>)
    abstract fun getData(date:String, liveData : MutableLiveData<GettingData?>, splashScreenOn : ObservableField<Boolean>?)


    fun refreshTokenByServer(refresh: String): Observable<RefreshTokenResponse> {

        networkHandler_.check()
        val request = HashMap<String,String>()
        request[REFRESH_TOKEN] = refresh
        return tokenService_.refreshToken(request)
    }

    fun  refreshToken(function: ()-> Unit) {
        networkHandler_.check()
        Loger.log("GEGIN refreshToken")
        compositeDisposable.add(
                refreshTokenByServer(loadRefreshToken().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({

                            val newAccesToken = it.data?.access?.value
                            val newRefreshToken = it.data?.refresh?.value
                            saveAccesToken(newAccesToken.toString())
                            saveRefreshToken(newRefreshToken.toString())

                            Loger.log("newAccesToken " + newAccesToken)
                            Loger.log("newRefreshToken " + newRefreshToken)

                            function()


                        }, {
                            Loger.log("---------------------------------refreshToken throwable " + it)
                            if (it.message== REFRESHTOKEN_EXPIRED){
                                expiredRefreshToken.value=true
                            }
                        })
        )
    }

    private fun loadRefreshToken(): String?{
        return tokenModel_.loadRefreshToken()
    }
    private fun loadAccesToken(): String?{
        return tokenModel_.loadAccesToken()
    }
    private fun saveRefreshToken(refreshToken: String){
        tokenModel_.saveRefreshToken(refreshToken)
    }
    private fun saveAccesToken(accesToken: String){
        tokenModel_.saveAccesToken(accesToken)
    }
}