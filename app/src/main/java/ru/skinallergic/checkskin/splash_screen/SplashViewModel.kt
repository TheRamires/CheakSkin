package ru.skinallergic.checkskin.splash_screen

import android.os.Handler
import android.os.Looper
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.news.NewsRepositoriy
import ru.skinallergic.checkskin.components.news.pojo.Datum
import ru.skinallergic.checkskin.components.news.pojo.QueryParamNews
import ru.skinallergic.checkskin.entrance.data.AccountRepositoriy
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.entrance.helper_classes.parserError
import ru.skinallergic.checkskin.entrance.pojo.Datass
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.shared_pref_model.UserConfigModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel @Inject constructor(val repo: SplashRepositoriy,
                                          val tokenModel: TokenModelImpls,
                                          val userModel: UserConfigModel,
                                          val toastyManager: ToastyManager,
                                          val newsRepositoriy: NewsRepositoriy,
                                          val accountRepositoriy: AccountRepositoriy
) : ViewModel() {
    val compositeDisposable=CompositeDisposable()
    var newsLive= MutableLiveData<List<Datum?>>()
    var profileLive= MutableLiveData<Datass>()
    var refreshTokenFailed=MutableLiveData<Boolean>()

    var goToIntro= MutableLiveData<Boolean>() //переходим на окно intro
    var goToLogIn= MutableLiveData<Boolean>() //переходим на окно logIn
    var goToHome= MutableLiveData<Boolean>() //переходим на окно home

    val complete= MutableLiveData<Boolean>()
    var progressBarOn: ObservableField<Boolean> = ObservableField(true)

    fun actionExample(){
        progressBarOn.set(false)
        Handler(Looper.getMainLooper()).postDelayed({
            progressBarOn.set(true)
            complete.value = true
        }, 2000)
    }

    fun checkEntrance(){
        progressBarOn.set(false)
        val firstEntrance=userModel.checkFirstEntrance()
        if (firstEntrance){
            goToIntro.value=true
        } else {
            checkLogIn()
        }
    }

    fun checkLogIn(){
        val logInIsTrue: Boolean= userModel.checkLogIn()

        if (logInIsTrue){
            refreshToken()
        } else {
            goToLogIn.value=true
        }
    }

    fun refreshToken(){
        val refresh=tokenModel.loadRefreshToken()
        refreshToken(refresh.toString())
    }

    fun refreshToken(refresh: String) {
        refreshTokenFailed.value=false
        Loger.log("GEGIN refreshToken")
        compositeDisposable.add(
                repo.refreshToken(refresh)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isSuccessful) {
                                //Loger.log("refreshToken "+it.string())
                                val response = it.body()
                                val newAccesToken = response?.data?.access?.value
                                val newRefreshToken = response?.data?.refresh?.value
                                tokenModel.saveAccesToken(newAccesToken.toString())
                                tokenModel.saveRefreshToken(newRefreshToken.toString())

                                Loger.log("newAccesToken " + newAccesToken)
                                Loger.log("newRefreshToken " + newRefreshToken)

                                //toastyManager.toastyyyy("refreshToken is true ")

                                loadHomeData()

                            } else if (it.code() == 422) {
                                Loger.log("REFRESH error 422")
                                val errorBody = it.errorBody()?.string()
                                Loger.log("errorBode \n" + errorBody)
                                val code = parserError(errorBody!!)
                                Loger.log("\n code " + code)
                                if (code == 1) {
                                    Loger.log("REFRESH code 1")
                                    //toastyManager.toastyyyy("Refresh токен устарел")
                                    goToLogIn.value = true // Токен устарел, переход на окно logIn ->
                                    return@subscribe
                                }
                            }

                        }, {
                            Loger.log("refreshToken throwable " + it.message)
                            toastyManager.toastyyyy("Не удалось обновить токен  ")
                            refreshTokenFailed.value=true

                        })
        )
    }

    fun loadHomeData(){
        getNews()
        getProfile()

    }

    fun getNews(){
        val queryParam = QueryParamNews("", "", 1, 5)
        compositeDisposable.add(newsRepositoriy.getNewsList(queryParam)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe( {
                    val response: List<Datum?> = it.data!!
                    newsLive.postValue(response)

                    /*val responseString=ConverterString.someObjectListToString(response)
                    Loger.log("responseString"+responseString)

                    val list: List<Datum?> =ConverterString.stringToSomeObjectList(responseString)

                    for (ent in list){
                        Loger.log(ent?.name)
                    }*/

                    //goToHome.postValue(true) //переход на окно home ----------------------->

                }, {
                    Loger.log("throwable "+it)
                })
        )
    }
    fun getProfile(){
        val accesToken=tokenModel.loadAccesToken()

        compositeDisposable.add(
                accountRepositoriy.getProfile(accesToken.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({

                            if (it.isSuccessful) {
                                val response=it.body()?.data
                                Loger.log("getProfile profile: \n" + it+"\n tel \n "+response?.tel)
                                val userData: Datass = response!!
                                profileLive.value=userData

                                //profileObservable.value = userData
                                //AccountViewModelImpl.NewUser.entity = lastUser()
                            }
                        }, {
                            toastyManager.toastyyyy("Не удалось загрузить данные")
                            Loger.log("getProfile THROWABLE: " + it+"☻☻")
                        })
        )
    }
}