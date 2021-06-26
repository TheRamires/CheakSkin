package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.HealthyDiaryRepository
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import javax.inject.Inject

class RatingViewModel @Inject constructor(val repository: HealthyDiaryRepository) : BaseViewModel() {
    override var baseRepository: BaseHealthyRepository =repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }

    val loaded= MutableLiveData<GettingData?>()

    val saved= MutableLiveData<Boolean>()
    val backSaved= MutableLiveData<Boolean>()
    val backLoaded= MutableLiveData<GettingData?>()

    var newRating: Int? = null
    var currentRating: Int? = null

    fun getData(date: Long){
        Loger.log("getDATA")
        baseRepository.getData((date / 1000).toString(), loaded, splashScreenOn)
    }


    fun save(date: Long,
    ){
        val writingData = WritingData(
                rating = newRating
        )
        baseRepository.redact((date / 1000).toString(), writingData, saved)
    }
    fun backSave(date: Long,
    ){
        val writingData = WritingData(
                rating = newRating
        )
        baseRepository.redact((date / 1000).toString(), writingData, backSaved)
    }

    fun backLoad(date: Long){
        splashScreenOn.set(true)
        baseRepository.getData((date / 1000).toString(), backLoaded,splashScreenOn)
    }
    fun isChanged(): Boolean{
        return (newRating!=null && newRating!=currentRating)
    }
    fun clearFieldsAfterRedact(){
        saved.value=false;
        backSaved.value=false;
        backLoaded.value=null;
        newRating=null;
    }
}