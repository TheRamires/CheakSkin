package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.HealthyDiaryRepository
import javax.inject.Inject

class HealthyDiaryViewModel  @Inject constructor(val repository: HealthyDiaryRepository) : BaseViewModel() {
    override var baseRepository: BaseHealthyRepository =repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }

    val isLoadedAndCalculated= MutableLiveData<Boolean>()
    val sum = ObservableField<Int>()
    fun getSumaryPercent(): Int{
        var sum: Int? = sum.get()
        if (sum == null) {
            sum = 0
        } else if (sum > 100) {
            sum = 100
        }
        return sum
    }
    val state = MutableLiveData<String>()

    val stateChecked=MutableLiveData<Boolean>()
    val rashPhotosChecked=MutableLiveData<Boolean>()
    val triggersChecked=MutableLiveData<Boolean>()
    val statmentChecked=MutableLiveData<Boolean>()
    val remindersChecked=MutableLiveData<Boolean>()
    val ratingChecked=MutableLiveData<Boolean>()

    fun data(date: Long){
        Loger.log("data start for view Model")
        compositeDisposable.add(
                repository.date((date / 1000).toString())
                        .doOnSubscribe { splashScreenOn.set(true) }
                        .doOnComplete { splashScreenOn.set(false) }
                        .subscribe({
                            val gettingData = it.data

                            val state = gettingData?.health_status
                            val rashPhotos = gettingData?.rashes
                            val triggers = gettingData?.triggers
                            val topicalTherapy = gettingData?.topical_therapy
                            val systemicTherapy = gettingData?.systemic_therapy
                            val otherTherapy = gettingData?.other_treatments
                            val rating = gettingData?.rating

                            defineStateString(state)

                            sumPlus(state, 20)
                            sumPlus(rashPhotos, 20)
                            sumPlus(triggers, 10)
                            sumPlus(topicalTherapy, 10)
                            sumPlus(systemicTherapy, 10)
                            sumPlus(otherTherapy, 10)
                            sumPlus(rating, 10)
                            //8

                            setCheckMarks(stateChecked,state)
                            setCheckMarks(rashPhotosChecked,rashPhotos)
                            setCheckMarks(triggersChecked,triggers)
                            setCheckMarks(statmentChecked, listOf(topicalTherapy,systemicTherapy,otherTherapy))
                            setCheckMarks(ratingChecked,rating)

                            isLoadedAndCalculated.value = true
                        }, {})
        )
    }
    fun  <T> setCheckMarks(liveData: MutableLiveData<Boolean>,value: T ){
        var bool=false
        when(value){
            null -> bool=false
            is String -> if (value!=""){bool=true}
            is Int -> bool=true
            is Collection<*> -> bool = value.size != 0
            else -> bool=false
        }
        liveData.value=bool
    }

    fun <T> sumPlus(value: T, percent: Int){
        var temp=sum.get()?:0
        temp = temp.plus(checkFullness(value, percent))
        sum.set(temp)
    }

    fun <T>checkFullness(value: T, percent: Int): Int{
        when(value){
            null -> return 0
            is String -> return return percent
            is Int -> return return percent
            is Collection<*> -> if (value.size == 0) {
                return 0
            } else return percent
            else -> return 0
        }
    }
    fun defineStateString(value: Int?){
        var string=""
        when(value){
            null->string="Не заполнено"
            0->string="Ремиссия"
            1->string="Подострое\n(хроническое)"
            2->string="Острое\n(обострение)"
        }
        state.value=string
    }
}