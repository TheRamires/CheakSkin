package ru.skinallergic.checkskin.shared_pref_model

import android.content.SharedPreferences
import javax.inject.Inject
private const val LOGIN="logIn"
private const val USERNAME="userName"
private const val FIRST="first"
class UserConfigModel @Inject constructor (private val sharedPreferences: SharedPreferences) {

    fun saveLogIn(logIn: Boolean) {
        val editor: SharedPreferences.Editor=sharedPreferences.edit()
        editor.putBoolean(LOGIN,true)
        editor.apply()
    }

    fun checkLogIn(): Boolean{
        return if (sharedPreferences.contains(LOGIN)){
            sharedPreferences.getBoolean(LOGIN,false)
        } else false
    }

    fun logOut(){
        val editor:SharedPreferences.Editor=sharedPreferences.edit()
        editor.putBoolean(LOGIN, false)
        editor.apply()
    }

    fun saveUserName(name: String){
        val editor: SharedPreferences.Editor=sharedPreferences.edit()
        editor.putString(USERNAME,name)
        editor.apply()
    }

    fun loadUserName(): String?{
        return if (sharedPreferences.contains(USERNAME)) {
            sharedPreferences.getString(USERNAME,"").toString()
        } else null
    }

    fun savefirstEntrance (){
        val editor: SharedPreferences.Editor=sharedPreferences.edit()
        editor.putBoolean(FIRST,false)
        editor.apply()
    }

    fun checkFirstEntrance(): Boolean{
        if (sharedPreferences.contains(FIRST)){
            return sharedPreferences.getBoolean(FIRST,true)
        } else return true
    }
}