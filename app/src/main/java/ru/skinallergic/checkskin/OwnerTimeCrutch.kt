package ru.skinallergic.checkskin

import androidx.lifecycle.MutableLiveData
import java.util.*

object OwnerTimeCrutch {
    fun crutch(liveData: MutableLiveData<Date>){
        liveData.value=Date()
    }
}