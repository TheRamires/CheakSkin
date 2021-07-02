package ru.skinallergic.checkskin.handlers

import android.content.Context
import android.widget.Toast
import ru.skinallergic.checkskin.Loger
import javax.inject.Inject

class ToastyManager @Inject constructor(val context: Context, val handleOnce: HandleOnce){
    fun toastyyyy(message: String){
        if (handleOnce.itWasNotHandled(message)){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        Loger.log("╝§toasty manager $message")
    }
}