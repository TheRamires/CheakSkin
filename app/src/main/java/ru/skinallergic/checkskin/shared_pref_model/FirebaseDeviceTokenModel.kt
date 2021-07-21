package ru.skinallergic.checkskin.shared_pref_model

import android.content.SharedPreferences
import ru.skinallergic.checkskin.Loger
import javax.inject.Inject

class FirebaseDeviceTokenModel @Inject constructor(private val sharedPreferences: SharedPreferences) {
    val FIREBASE="firebaseDeviceToken"

    fun load(): String?{
        if(sharedPreferences.contains(FIREBASE)) {
            Loger.log(" ♦ LOAD. FIREBASE "+sharedPreferences.getString(FIREBASE," "))
            val loadTok=sharedPreferences.getString(FIREBASE,"")
            return loadTok
        } else {
            Loger.log(" ♦ LOAD. FIREBASE is not found") }
        return null
    }

    fun save(firebaseToken: String){
        Loger.log(" ♦ SAVE. FIREBASE " +firebaseToken)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(FIREBASE, firebaseToken)
        editor.apply()
    }
}