package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.components.healthdiary.repositories.AffectedArreaRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import java.util.*
import javax.inject.Inject

class AffectedAreaViewModel @Inject constructor(val repository: AffectedArreaRepository): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository =repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }
    val redactModeLive= MutableLiveData<Boolean>()
    val redactModeobservable= ObservableField<Boolean>()

    var stumpForImageView: Boolean = false
    var stumpForButtonClose: Boolean = false

    fun redactModeOn(){
        redactModeLive.value=true
        redactModeobservable.set(true)
    }
    fun redactModeOff(){
        redactModeLive.value=false
        redactModeobservable.set(false)
    }

    fun redactModeIsOn():Boolean{
        return !(redactModeLive.value ==null || redactModeLive.value==false)
    }
}
