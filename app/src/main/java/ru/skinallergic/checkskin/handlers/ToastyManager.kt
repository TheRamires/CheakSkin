package ru.skinallergic.checkskin.handlers

import android.content.Context
import android.widget.Toast
import ru.skinallergic.checkskin.Loger
import javax.inject.Inject

class ToastyManager @Inject constructor(val context: Context){
    fun toastyyyy(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Loger.log("toasty manager $message")
    }
}