package ru.skinallergic.checkskin.components.tests.data

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.skinallergic.checkskin.Api.TestResultService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.entrance.helper_classes.parserError
import ru.skinallergic.checkskin.entrance.pojo.RefreshTokenResponse
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.utils.REFRESHTOKEN_EXPIRED
import ru.skinallergic.checkskin.utils.REFRESH_TOKEN
import ru.skinallergic.checkskin.utils.TOKENEXPIRED
import javax.inject.Inject

class Repository @Inject constructor(val storage: Storage,
                                     val service: TestResultService,
                                     var tokenModel: TokenModelImpls,
                                     val networkHandler: NetworkHandler,
                                     val toastyManager: ToastyManager) {

    lateinit var expiredRefreshToken :MutableLiveData<Boolean>
    val compositeDisposable= CompositeDisposable()

    fun getTests(): List<EntityTest>{
        return storage.tests
    }

    fun sendResult(test: String, sum: String){
        networkHandler.check()
        val result = HashMap<String,String>()
        result.put("result",sum)
        if (loadAccesToken()==null){
            return
        }
        loadAccesToken()?.let { accesToken->
            service.sendResult(accesToken, test, result)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({

                    },{

                        Loger.log("*************************2>>> ${it.message}")
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            (refreshToken { sendResult(test, sum) })
                        }
                    })
        }
    }

    fun getResult(test: String,
                  liveData: MutableLiveData<List<TestResult>?> ){
        networkHandler.check()
        if (loadAccesToken()==null){
            return
        }
        loadAccesToken()?.let {
            compositeDisposable.add(service.getResult(it, test)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        liveData.value=it.data

                    }) {
                        Loger.log("**************getResult***********2>>> ${it.message}")
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            (refreshToken { getResult(test, liveData) })
                        }
                    })
        }!!

    }
    fun refreshTokenByServer(refresh: String): Observable<RefreshTokenResponse>{
        networkHandler.check()
        val request = HashMap<String,String>()
        request[REFRESH_TOKEN] = refresh
        return service.refreshToken(request)
    }

    fun  refreshToken(function: ()-> Unit) {
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
                            if (it.message==REFRESHTOKEN_EXPIRED){
                                expiredRefreshToken.value=true
                            }
                        })
        )
    }

    private fun loadRefreshToken(): String?{
        return tokenModel.loadRefreshToken()
    }
    private fun loadAccesToken(): String?{
        return tokenModel.loadAccesToken()
    }
    private fun saveRefreshToken(refreshToken: String){
        tokenModel.saveRefreshToken(refreshToken)
    }
    private fun saveAccesToken(accesToken: String){
        tokenModel.saveAccesToken(accesToken)
    }
}