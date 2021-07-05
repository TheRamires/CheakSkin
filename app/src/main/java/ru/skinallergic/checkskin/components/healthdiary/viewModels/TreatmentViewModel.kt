package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.HealthyDiaryRepository
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import javax.inject.Inject
const val TOPICAL_THERAPY_INDEX=0
const val SYSTEMATIC_THERAPY_INDEX=1
const val OTHER_TREATMENTS_INDEX=2
const val THERAPIES_BUNDLE="therapiesBundle"

class TreatmentViewModel @Inject constructor(val repository: HealthyDiaryRepository) : BaseViewModel() {

    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }

    val loaded= MutableLiveData<GettingData?>()
    val saved= MutableLiveData<Boolean>()
    val backSaved= MutableLiveData<Boolean>()
    val backLoaded= MutableLiveData<GettingData?>()

    val therapiesOld = mutableListOf<String>("","","")
    var therapiesNew = mutableListOf<String>("","","")
    val checkMarks = mutableListOf<Boolean>(false,false,false)
    val marksLive = MutableLiveData<List<Boolean?>>()

    fun save(date: Long,
             therapiesNew: List<String>,
    ){
        val writingData = WritingData(
                topical_therapy=therapiesNew[TOPICAL_THERAPY_INDEX],
                systemic_therapy=therapiesNew[SYSTEMATIC_THERAPY_INDEX],
                other_treatments=therapiesNew[OTHER_TREATMENTS_INDEX]
        )
        repository.redact((date/1000).toString(), writingData, saved,progressBar)
    }

    fun getData(date: Long){
        Loger.log("getDATA")
        splashScreenOn.set(true)
        repository.getData((date/1000).toString(),loaded,splashScreenOn)
    }

    fun backSave(date: Long,
                 therapiesNew: List<String>,
    ){
        val writingData = WritingData(
                topical_therapy=therapiesNew[TOPICAL_THERAPY_INDEX],
                systemic_therapy=therapiesNew[SYSTEMATIC_THERAPY_INDEX],
                other_treatments=therapiesNew[OTHER_TREATMENTS_INDEX]
        )
        repository.redact((date/1000).toString(), writingData, backSaved,progressBar)
    }

    fun backLoad(date: Long){
        splashScreenOn.set(true)
        repository.getData((date/1000).toString(),backLoaded,splashScreenOn)
    }
    fun initTherapiesOld(list: List<String>){
        Loger.log("initTherapiesOld $list")
        for (i in checkMarks.indices){
            checkMarks[i] = list[i] !=""
            therapiesOld[i]=list[i]
            therapiesNew[i]=list[i]
            Loger.log("checkMarks $i = ${checkMarks[i]}")
            Loger.log("")
        }
        marksLive.value=checkMarks
    }
    fun changeMark(index: Int, value: Boolean){
        checkMarks[index]=value
        marksLive.value=checkMarks
    }

    fun checkMarkChanged():Boolean{  //хотя бы одно поле заполнено
        var boolean=false
        for(checkMark in checkMarks){
            if (checkMark){
                Loger.log("checkMark $checkMark")
                boolean= true
            }
        }
        Loger.log("checkMarkChanged $boolean")
        return boolean
    }
    fun therapyListIsChanged():Boolean{
        Loger.log("isChanged ${!(therapiesOld == therapiesNew)}")
        Loger.log("therapiesOld $therapiesOld")
        Loger.log("therapiesNew $therapiesNew")
        return !(therapiesOld == therapiesNew)
    }

    fun isChanged(): Boolean{
        if (!checkMarkChanged()){
            return false

        } else {
            return therapyListIsChanged()
        }
    }
}