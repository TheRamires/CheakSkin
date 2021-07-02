package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.HealthyDiaryRepository
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import javax.inject.Inject

class StateViewModel @Inject constructor(val repository: HealthyDiaryRepository) : BaseViewModel() {

    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }

    val loaded= MutableLiveData<GettingData?>()
    val saved= MutableLiveData<Boolean>()
    val backSaved= MutableLiveData<Boolean>()
    val backLoaded= MutableLiveData<GettingData?>()

    fun save(date: Long,
             health_status: Int
    ){
        val writingData = WritingData(
                health_status=health_status
                )
        repository.redact((date/1000).toString(), writingData, saved,progressBar)
    }

    fun getData(date: Long){
        Loger.log("getDATA")
        repository.getData((date/1000).toString(),loaded,splashScreenOn)
    }


    fun backSave(date: Long,
                 health_status: Int
    ){
        val writingData = WritingData(
                health_status=health_status
        )
        repository.redact((date/1000).toString(), writingData, backSaved,progressBar)
    }

    fun backLoad(date: Long){
        repository.getData((date/1000).toString(),backLoaded,splashScreenOn)
    }
    fun isChanged(old: Int?, new: Int?): Boolean{
        Loger.log("old $old new $new")
        if(old==null && new==null){
            return false
        } else return old!=new
    }
}