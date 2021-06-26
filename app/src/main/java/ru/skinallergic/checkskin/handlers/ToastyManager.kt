package ru.skinallergic.checkskin.handlers

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class ToastyManager @Inject constructor(val context: Context){
    fun toastyyyy(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}