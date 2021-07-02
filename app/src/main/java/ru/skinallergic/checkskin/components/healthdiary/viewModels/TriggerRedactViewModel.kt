package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.HealthyDiaryRepository
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import javax.inject.Inject

class TriggerRedactViewModel @Inject constructor(val repository: HealthyDiaryRepository): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository =repository

    //----------------------------------------------------****
    val oldChoosingTriggers :MutableList<Int> = arrayListOf()
    val newChoosingTriggers =MutableLiveData<MutableList<Int>>()

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        newChoosingTriggers.value= arrayListOf()
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }
    val saved= MutableLiveData<Boolean>()
    val backSaved= MutableLiveData<Boolean>()
    val backLoaded= MutableLiveData<GettingData?>()

    fun save(date: Long,
                  triggers: Array<Int>?=null

    ){
        val writingData = WritingData(
                null,
                null,
                null,
                null,
                null,
                triggers)
        repository.redact((date/1000).toString(), writingData, saved,progressBar)
    }

    fun backSave(date: Long,
                 triggers: Array<Int>?=null

    ){
        val writingData = WritingData(
                null,
                null,
                null,
                null,
                null,
                triggers)
        repository.redact((date/1000).toString(), writingData, backSaved,progressBar)
    }

    fun compareChanging(): Boolean{
        return oldChoosingTriggers != newChoosingTriggers.value
    }

    fun baclLoad(date: Long){
        repository.getData((date/1000).toString(),backLoaded,splashScreenOn)
    }
}