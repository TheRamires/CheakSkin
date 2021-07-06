package ru.skinallergic.checkskin.view_models

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.entrance.VKUsersRequest
import ru.skinallergic.checkskin.entrance.data.AccountRepositoriy
import ru.skinallergic.checkskin.entrance.data.HardcoreData
import ru.skinallergic.checkskin.entrance.data.RegistrationRepositoriy
import ru.skinallergic.checkskin.entrance.data.UserEntity
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorField
import ru.skinallergic.checkskin.entrance.helper_classes.parserError
import ru.skinallergic.checkskin.entrance.pojo.*
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.shared_pref_model.UserConfigModel
import ru.skinallergic.checkskin.utils.CHOOSE_REGIONS
import ru.skinallergic.checkskin.utils.GENDER_NONE
import ru.skinallergic.checkskin.utils.TOKEN_EXPIRED
import java.util.*
import javax.inject.Inject

class AccountViewModelImpl @Inject constructor(val accountRepo: AccountRepositoriy,
                                               val registrationRepo: RegistrationRepositoriy,
                                               val hardcoreData: HardcoreData,
                                               val tokenModel: TokenModelImpls,
                                               val userModel: UserConfigModel,
                                               val toastyManager: ToastyManager,
                                               val validatorField: ValidatorField
): AccountViewModel() {
    var splashScreen = ObservableField<Boolean?>()
    var expiredRefreshToken=MutableLiveData<Boolean>()
    var compositeDisposable=CompositeDisposable()
    var stubNumberChanging = false

    //redact account------------------------------------------------------------------------

    var redactComplete=MutableLiveData<Boolean>()
    var changeNumber=MutableLiveData<Boolean>()
    val profileObservable= MutableLiveData<Datass?>()

    object NewUser{
        var entity: UserEntity = UserEntity()
    }

    fun lastUser(): UserEntity {
        var lastUser= UserEntity()
        lastUser.apply {
            val profileEntity=profileObservable.value

            name= profileEntity?.name?: ""
            tel=profileEntity?.tel?: ""
            regionId= profileEntity?.region?.id
            diagnosisId=profileEntity?.diagnosis?.id
            gender=profileEntity?.gender?:GENDER_NONE
        }
        return lastUser
    }
    fun redactNumber(){
        if (!NewUser.entity.tel.equals(lastUser().tel)) {
            var newNumber= NewUser.entity.tel
            if (newNumber?.get(0)?.toString().equals("+")){
                newNumber=newNumber?.substring(1)
            }
            checkNumber(newNumber!!)
        }
    }

    fun redactUser(newUser: UserEntity){
        NewUser.entity =newUser
        redactUser()
    }
    fun redactName(){
        if (!NewUser.entity.name.equals(lastUser().name)){
            val nameIsCorrect=validatorField.validate(NewUser.entity)
            if (nameIsCorrect){
                redactProfile(NewUser.entity)
            }

            /*val isTrue:Boolean = validatorField.validateName(NewUser.entity.name)
            if (isTrue) {
                redactProfile(NewUser.entity)
            }*/
        }
    }
    fun changeNumber(): Boolean{
        Loger.log("changeNumber")
        if (!NewUser.entity.tel.equals(lastUser().tel)){
            checkNumber(NewUser.entity.tel.toString())
            //changeNumber.value=true
            return true
        } else return false
    }

    fun checkUser():Boolean{
        Loger.log("redactUser")
        //В случае ошибки сервера поля будут пустыми. Нужно предотвратить редактирование в таком случае
        if (NewUser.entity.name =="" || NewUser.entity.diagnosisId==0 || NewUser.entity.regionId==0
                || NewUser.entity.tel=="" || NewUser.entity.gender==-1){
            return false

        } else if (NewUser.entity.equals(lastUser())){
            Loger.log("1 current user: " + lastUser())
            Loger.log("1 new user: " + NewUser.entity)
            Loger.log("1 true")
            return false
/*
        } else if (checkNumber()) {
            changeNumber.value=true
            return true*/

        } else {
            Loger.log("2 current user: " + lastUser())
            Loger.log("2 new user: " + NewUser.entity)
            Loger.log("2 false")
            return true
        }
    }

    fun redactUser(){
        Loger.log("redactUser")
        //В случае ошибки сервера поля будут пустыми. Нужно предотвратить редактирование в таком случае
        if (NewUser.entity.name =="" || NewUser.entity.diagnosisId==0 || NewUser.entity.regionId==0
               || NewUser.entity.tel=="" || NewUser.entity.gender==-1){
            return

        } else if (NewUser.entity.equals(lastUser())){
            Loger.log("1 current user: " + lastUser())
            Loger.log("1 new user: " + NewUser.entity)
            Loger.log("1 true")
            return
/*
        } else if (checkNumber()) {
            changeNumber.value=true
            redactProfile(NewUser.entity)*/

        } else {
            Loger.log("2 current user: " + lastUser())
            Loger.log("2 new user: " + NewUser.entity)
            Loger.log("2 false")
            redactProfile(NewUser.entity)
        }
    }

    //override functions----------------------------------------------------------------------

    override fun redactProfile(user: UserEntity){
        val accesToken=loadAccesToken()
        var userTemp=currentUser.value
        userTemp?.name=user.name
        currentUser.value=userTemp
        //currentUser.name=user.name
        var profile= ProfileRequest(
                user.name,
                user.gender,
                user.regionId,
                user.diagnosisId
        )
        Loger.log("redactProfile ProfileRequest $profile")
        compositeDisposable.add(
                accountRepo.redactProfile(accesToken.toString(), profile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isSuccessful) {
                                toastyManager.toastyyyy("Данные пользователя сохранены")
                                profileObservable.value = it.body()!!.data!!
                                Loger.log("redactProfile profile: " + it.body())
                            } else if (it.code() == 401) {
                                val code = parserError(it.errorBody()!!.string())
                                Loger.log("\n code " + code)
                                if (code == 4) {
                                    cheakTokenExpired(code, { redactProfile(user) })
                                }
                            }

                        }, { Loger.log("redactProfile THROWABLE: $it ☻") }
                        )
        )
    }

    override fun redactNumber(number: String, sendingCode: String){
        val accesToken=loadAccesToken()
        val redactNumberRequest=RedactNumberRequest(number, sendingCode)
        Loger.log("number " + number)
        compositeDisposable.add(
                accountRepo.redactNumber(accesToken.toString(), redactNumberRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isSuccessful) {
                                //toastyManager.toastyyyy("Данные пользователя сохранены")
                                Loger.log("redactNumber isSuccessful: " + it.body())
                                redactComplete.value = true
                            } else if (it.code() == 401) {
                                Loger.log("redactProfile error 401: ")
                                val code = parserError(it.errorBody()!!.string())
                                Loger.log("\n code " + code)
                                if (code == 4) {
                                    cheakTokenExpired(code, { redactNumber(number, sendingCode) })
                                }
                            } else {
                                Loger.log("redactNumber error??? ${it.code()}")
                            }

                            /*
                    when(it.code()){
                        200-> {
                            redactComplete.value = true
                            toastyManager.toastyyyy("Номер телфона изменен успешно")
                            Loger.log("redactNumber: " + it.body()?.message)
                        }
                        401->{
                            Loger.log("401 \n"+it.errorBody()!!.string())
                            val code= parserError(it.errorBody()!!.string())
                            Loger.log("\n code "+code)
                            if (code==4){
                                cheakTokenExpired(code, { redactNumber(number, sendingCode) })
                            } else Loger.log(it.message() + " ☻")
                        } ????????????????????????????????????????????????????????????????????????
                    }*/
                        }, {
                            Loger.log(it.message + " ☻")
                        })
        )
    }

    override fun getProfile(){
        val accesToken=loadAccesToken()
        Loger.log("*************************/ 2 "+accesToken)
        compositeDisposable.add(
                accountRepo.getProfile(accesToken.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        //.retryWhen { throwableObservable -> Loger.log("repeat getProfile ☺");throwableObservable.take(3).delay(1, TimeUnit.SECONDS) }
                        .doOnSubscribe(Consumer { splashScreen.set(true) })
                        .doOnComplete({ splashScreen.set(false) })
                        .subscribe({

                            if (it.isSuccessful) {
                                val userData: Datass = it.body()?.data!!
                                Loger.log("getProfile profile: ")
                                Loger.log("getProfile profile: \n" + it + "\n name " + userData.name)
                                profileObservable.value = userData

                                var user=currentUser.value
                                user?.setData(userData)
                                currentUser.value=user
                                //currentUser.setData(userData)
                                NewUser.entity = lastUser()

                            } else if (it.code() == 401) {
                                val errorBody = it.errorBody()?.string()
                                Loger.log("errorBode \n" + errorBody)
                                val code = parserError(errorBody!!)
                                Loger.log("\n code " + code)
                                if (code == 4) {
                                    Loger.log("cheakTokenExpired $code")
                                    cheakTokenExpired(code, { getProfile() })
                                } else{

                                }
                            } else {
                                Loger.log("getProfile profile:" + it.message())
                                toastyManager.toastyyyy(it.message())
                            }
                        }, {
                           // toastyManager.toastyyyy("THROWABLE: " + it + "☻☻")
                            toastyManager.toastyyyy("Не удалось обновить данные. "+it.message)
                            Loger.log("getProfile THROWABLE: " + it + "☻☻")
                        })
        )
    }
    fun deleteAccount(){
        deleteAccount(false)
    }

    override fun deleteAccount(notToast: Boolean){
        val accesToken=loadAccesToken()
        Loger.log(accesToken)
        compositeDisposable.add(
                accountRepo.deleteAccount(accesToken.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isSuccessful) {
                                val response = it.body()?.data
                                Loger.log("deleteAccount : \n" + it)
                                if (!notToast) {
                                    toastyManager.toastyyyy("Аккаунт удален")
                                }
                                liveQuit.value = true
                                userModel.logOut()
                                clearCurrentUser()
                            } else if (it.code() == 401) {
                                val errorBody = it.errorBody()?.string()
                                Loger.log("errorBode \n" + errorBody)
                                val code = parserError(errorBody!!)
                                Loger.log("\n code " + code)
                                if (code == 4) {
                                    Loger.log("cheakTokenExpired $code")
                                    cheakTokenExpired(code, { deleteAccount() })
                                }
                            }

                        }, {
                            if (!notToast) {
                                toastyManager.toastyyyy("Удаление аккаунта закочилось ошибкой")
                            }
                            Loger.log("delete throwable" + it)
                        })
        )
    }

    override fun quit() {
        val accesToken=loadAccesToken()
        compositeDisposable.add(
                accountRepo.quit(accesToken.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        //.retryWhen { throwableObservable -> Loger.log("repeat quit ☺");throwableObservable.take(3).delay(1, TimeUnit.SECONDS) }
                        .subscribe({
                            if (it.isSuccessful) {
                                val response = it.body()?.data
                                Loger.log("QuitAccount : \n" + it)
                                toastyManager.toastyyyy("Вы вышли из аккаунта")
                                liveQuit.value = true
                                userModel.logOut()
                                clearCurrentUser()
                            } else if (it.code() == 401) {
                                val errorBody = it.errorBody()?.string()
                                Loger.log("errorBode \n" + errorBody)
                                val code = parserError(errorBody!!)
                                Loger.log("\n code " + code)
                                if (code == 4) {
                                    toastyManager.toastyyyy(" Выход, Токен устарел")
                                    Loger.log("cheakTokenExpired $code")
                                    cheakTokenExpired(code, { quit() })
                                }
                            }

                        }, {
                            toastyManager.toastyyyy("Выход не был завершен успешно")
                            Loger.log("quit throwable" + it)
                        })
        )
    }

    override fun refreshToken(function: () -> Unit) {
        Loger.log("GEGIN refreshToken")
        compositeDisposable.add(
                accountRepo.refreshToken(loadRefreshToken().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isSuccessful) {
                                //Loger.log("refreshToken "+it.string())
                                val response = it.body()
                                val newAccesToken = response?.data?.access?.value
                                val newRefreshToken = response?.data?.refresh?.value
                                saveAccesToken(newAccesToken.toString())
                                Loger.log("*******************************3 save newAccesToken "+newAccesToken)
                                saveRefreshToken(newRefreshToken.toString())

                                Loger.log("newAccesToken " + newAccesToken)
                                Loger.log("newRefreshToken " + newRefreshToken)

                                function()
                            } else if (it.code() == 422) {
                                Loger.log("REFRESH error 422")
                                val errorBody = it.errorBody()?.string()
                                Loger.log("errorBode \n" + errorBody)
                                val code = parserError(errorBody!!)
                                Loger.log("\n code " + code)
                                if (code == 1) {
                                    Loger.log("REFRESH code 1")
                                    toastyManager.toastyyyy("Устаревший refresh token")
                                    expiredRefreshToken.value = true
                                    return@subscribe
                                }
                            }

                        }, {
                            Loger.log("refreshToken throwable " + it)
                        })
        )
    }

    override fun getRegions(){
        compositeDisposable.add(accountRepo.getRegions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.retryWhen { throwableObservable -> Loger.log("repeat getRegions ☺");throwableObservable.take(2).delay(1, TimeUnit.SECONDS) }
                .doOnSubscribe(Consumer { splashScreen.set(true) })
                .doOnComplete({ splashScreen.set(false) })
                .subscribe({

                    var temp: MutableList<DatumR> = it.data as MutableList<DatumR>
                    temp.sortWith { o1, o2 -> o1.id.compareTo(o2.id) }

                    var listRegions: MutableList<String> = arrayListOf()
                    listRegions.add(CHOOSE_REGIONS)
                    for (row in 0 until temp.size) {
                        listRegions.add(temp[row].name.toString())
                    }
                    Loger.log("listRegions " + listRegions)

                    liveRegions.value = listRegions
                }, {
                    Loger.log("getRegions THROWABLE " + it)

                })
        )
        //val list=hardcoreData.getRegions() hardcore
        //liveRegions.value=list
    }

    override fun getDiagnosis(){
        val list=hardcoreData.getDiagnosis()
        liveDianosis.value=list
    }

    private fun cheakTokenExpired(errorCode: Int, function: () -> Unit){
        if (errorCode ==TOKEN_EXPIRED){
            toastyManager.toastyyyy("Токен устарел, ошибка $errorCode, идет обновление")
            Loger.log("Токен устарел, ошибка $errorCode, идет обновление")

            refreshToken(function)
        }
    }

    private fun loadRefreshToken(): String?{
        return tokenModel.loadRefreshToken()
    }
    public fun loadAccesToken(): String?{
        return tokenModel.loadAccesToken()
    }
    private fun saveRefreshToken(refreshToken: String){
        tokenModel.saveRefreshToken(refreshToken)
    }
    private fun saveAccesToken(accesToken: String){
        tokenModel.saveAccesToken(accesToken)
    }

    //-------------------------Registration---------------------------------------------------

    override fun checkNumber(number_: String){
        var number=number_
        if (number_[0].toString().equals("+")){number=number_.substring(1)}
        compositeDisposable.add(
                registrationRepo.checkNumber(number)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response ->
                            if (response.data?.success == false) {
                                toastyManager.toastyyyy("Такого номера не существует")
                            } else {
                                Loger.log("cheakNumber")
                            }
                            val result = response.data?.success; Loger.log(result)
                            Loger.log("number is exist")
                            numberIsCheacked.value = result!!  //for log_in
                            //changeNumber.value=true //for profile change_number

                        }, { error ->
                            Loger.log(error.message + " ♦")
                            error.printStackTrace()
                        })
        )
    }

    override fun sendingCode(number_: String){
        var number=number_
        if (number_[0].toString().equals("+")){
            number=number_.substring(1)
        }
        compositeDisposable.add(
                registrationRepo.sendingCode(number)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            toastyManager.toastyyyy(it.data?.code.toString())
                            Loger.log("The confirmation code was successfully sent, code: ${it.data?.code}")

                        }, { error ->
                            toastyManager.toastyyyy("Неверный код подтверждения")
                            Loger.log(error.message + " ☻")
                            error.printStackTrace()
                        })
        )
    }

    override fun logIn(number_: String){
        var number=number_
        if (number_[0].toString().equals("+")){
            number=number_.substring(1)
        }
        if (code == ""){
            Loger.log("Запрос на сервер, проверка кода -☻")
            return
        }
        compositeDisposable.add(
                registrationRepo.loginRequest(number, code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Loger.log("Log In Response " + it.data?.user?.name)

                            val refreshToken = it.data?.tokens?.refresh?.value
                            val accesToken = it.data?.tokens?.access?.value
                            Loger.log("////////////////////////////////// accesToken "+accesToken)
                            saveRefreshToken(refreshToken.toString())
                            saveAccesToken(accesToken.toString())
                            Loger.log("////////////////////////////////// loadAccesToken "+loadAccesToken())

                            val name: String? = it.data?.user?.name
                            if (name == null || name.equals("")) {
                                registrationCompleted.value = true
                            } else {
                                authorizationCompleted.value = true

                                var user=currentUser.value
                                user?.name=name
                                currentUser.value=user
                                //currentUser.name=name //!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 24.05
                            }
                        }, { error ->
                            Loger.log(error.message + " ☻")
                        })
        )
    }

    override fun netWorkLogIn(network: String, token: String) {
        compositeDisposable.add(
                registrationRepo.netWorklogIn(network, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Loger.log("netWorkLogIn "+it.data)

                            val refreshToken = it.data?.tokens?.refresh?.value
                            val accesToken = it.data?.tokens?.access?.value
                            saveRefreshToken(refreshToken.toString())
                            saveAccesToken(accesToken.toString())
                            val user=it.data?.user

                            if (user?.name!=null && user?.name!="" &&
                                    user?.gender!=null &&
                                    user?.diagnosis!=null && user?.region!=null){
                                authorizationCompleted.value=true
                            } else {
                                registrationCompleted.value=true
                            }

                            /*Loger.log("netWorkLogIn " + it.message)
                            toastyManager.toastyyyy("Заполните недостающие данные")*/

                        }, { error ->
                            Loger.log("netWorkLogIn " + error.message)
                            toastyManager.toastyyyy(error.message.toString())
                        })
        )
    }
     override fun getVkUser(){
        compositeDisposable.add(Observable.fromCallable {
            VK.executeSync(VKUsersRequest())
        }
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    var user=currentUser.value
                    user?.name=it[0].firstName
                    currentUser.value=user
                    //currentUser.name=it[0].firstName
                    Loger.log("name "+it[0].firstName)                    // response here
                }, {
                    // throwable here
                }))
    }

    override fun onCleared() {
        super.onCleared()
        clearAllSubscribes()
    }

    fun clearAllSubscribes(){
        Loger.log("отписка RX от accountViewModel")
        compositeDisposable.clear()
    }
    override fun logInSave(){
        userModel.saveLogIn(true)
    }

    override fun saveFirstEntrance() {
        userModel.savefirstEntrance()
    }
    private fun clearCurrentUser(){
        /*currentUser.apply {
            name=null
            tel=null
            diagnosisId=null
            regionId=null
            gender=null
        }*/
        currentUser.value=null
    }
}