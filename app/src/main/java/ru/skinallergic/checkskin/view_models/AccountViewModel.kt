package ru.skinallergic.checkskin.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.entrance.data.UserEntity

abstract class AccountViewModel: ViewModel() {
    var liveRegions = MutableLiveData<List<String>>()
    var liveDianosis = MutableLiveData<List<String>>()

    abstract fun getRegions()
    abstract fun getDiagnosis()

    abstract fun redactProfile(user: UserEntity)
    abstract fun redactNumber(number: String, code: String)
    abstract fun getProfile()
    abstract fun deleteAccount(notToast : Boolean)
    abstract fun quit()

    var liveQuit=MutableLiveData<Boolean>()

    abstract fun refreshToken(function: ()->Unit)
    abstract fun logInSave()
    abstract fun saveFirstEntrance()

    //Registration-----

    var currentUser = MutableLiveData<UserEntity>(UserEntity())

    var code: String=""
    var fieldIsFull: Boolean = false
    val numberIsCheacked = MutableLiveData<Boolean>()
    val registrationCompleted= MutableLiveData<Boolean>()
    val authorizationCompleted= MutableLiveData<Boolean>()

    //redact-----------

    abstract fun checkNumber(number_: String)
    abstract fun sendingCode(number_: String)
    abstract fun logIn(number_: String)
    abstract fun netWorkLogIn(network: String, token: String)
    abstract fun getVkUser()

}