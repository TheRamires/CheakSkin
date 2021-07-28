package ru.skinallergic.checkskin.geolocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import ru.skinallergic.checkskin.Loger

object GeoPermission {
    fun check(context: Context): Boolean{
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // делаем что-то, если геолокация не включена
            Loger.log("---------------------геолокация не включена!!-----------------------")
            return false
        } else {
            Loger.log("---------------------☺☺☺☺☺☺ ГЕОЛОКАЦИЯ ВКЛЮЧЕНА---------------------")
            return true
        }
    }
}