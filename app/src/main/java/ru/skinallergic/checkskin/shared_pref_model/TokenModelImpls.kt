package ru.skinallergic.checkskin.shared_pref_model

import android.content.SharedPreferences
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.utils.ACCESS
import ru.skinallergic.checkskin.utils.REFRESH
import javax.inject.Inject
import javax.inject.Singleton

class TokenModelImpls @Inject constructor (private val sharedPreferences: SharedPreferences): TokenModel {

    override fun loadRefreshToken(): String?{
        if(
                sharedPreferences.contains(REFRESH)) {
            Loger.log(" ♦ LOAD REFRESHTOKEN "+sharedPreferences.getString(REFRESH," "))
            return sharedPreferences.getString(REFRESH,"")
        }
        return null
    }
    override fun loadAccesToken(): String?{
        if(sharedPreferences.contains(ACCESS)) {
            Loger.log(" ♦ LOAD ACCESSTOKEN "+sharedPreferences.getString(ACCESS," "))
            val loadTok=sharedPreferences.getString(ACCESS,"")
            val accesToken="Token "+loadTok
            return accesToken
        } else {
            Loger.log(" ♦ LOAD ACCESSTOKEN is not found") }
        return null
    }

    override fun saveRefreshToken(refreshToken: String){

        Loger.log(" ♦ SAVE REFRESHTOKEN " +refreshToken)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(REFRESH, refreshToken)
        editor.apply()
    }
    override fun saveAccesToken(accesToken: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        Loger.log(" ♦ SAVE accesToken " +accesToken)
        //editor.remove(ACCESS)
        editor.putString(ACCESS, accesToken)
        editor.apply()
    }

    //при выходе из приложения
    override fun deleteAccesToken() {
        if(sharedPreferences.contains(ACCESS)){
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.remove(ACCESS)
            editor.apply()
            loadAccesToken() //Проверка
        }
    }
}