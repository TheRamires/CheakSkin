package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.remote.Rash
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.HealthyDiaryRepository
import javax.inject.Inject

const val STATE_PERCENT=20
const val RASHPHOTOS_PERCENT=30
const val TRIGGERS_PERCENT=20
const val TOPIC_THERAPY_PERCENT=5
const val SYSTEMATIC_THERAPY_PERCENT=5
const val OTHER_THERAPY_PERCENT=5
const val REMINDER_PERCENT=5
const val RATE_PERCENT=10

class HealthyDiaryViewModel  @Inject constructor(val repository: HealthyDiaryRepository) : BaseViewModel() {
    override var baseRepository: BaseHealthyRepository =repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }

    val isLoadedAndCalculated= MutableLiveData<Boolean>()
    val sum = ObservableField<Int>()
    var cacheSum = ObservableField<Int>()
    fun setCacheSum(value: Int){
        cacheSum.set(value)
    }

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

                            val state: Int? = gettingData?.health_status
                            val rashPhotos: List<Rash>? = gettingData?.rashes
                            val triggers: Array<Int>? = gettingData?.triggers
                            val topicalTherapy: String? = gettingData?.topical_therapy
                            val systemicTherapy: String? = gettingData?.systemic_therapy
                            val otherTherapy: String? = gettingData?.other_treatments
                            val rating: Int? = gettingData?.rating

                            defineStateString(state)

                            sumPlus(state, STATE_PERCENT)
                            sumPlus(rashPhotos, RASHPHOTOS_PERCENT)
                            sumPlus(triggers, TRIGGERS_PERCENT)
                            sumPlus(topicalTherapy, TOPIC_THERAPY_PERCENT)
                            sumPlus(systemicTherapy, SYSTEMATIC_THERAPY_PERCENT)
                            sumPlus(otherTherapy, OTHER_THERAPY_PERCENT)
                            sumPlus(rating, RATE_PERCENT)
                            //8
                            changeMark(stateChecked, state)
                            changeMark(rashPhotosChecked, rashPhotos)
                            changeMark(triggersChecked, triggers)
                            changeMark(statmentChecked, listOf(topicalTherapy,systemicTherapy,otherTherapy ))
                            changeMark(remindersChecked, null)
                            if (rating==null || rating==0){
                                changeMark(ratingChecked, null)
                            } else if( rating>0){
                                changeMark(ratingChecked, rating)
                            }

                            isLoadedAndCalculated.value = true
                            setCacheSum(sum.get()!!)
                        }, {Loger.log("********************о☻ in healthyDiaryViEModel \n"+it)
                            errorCallBack()
                        })
        )
    }
    fun errorCallBack(){
        //user cacheSum
        Loger.log("cacheSum ${cacheSum.get()}")
        sum.set(cacheSum.get())
        isLoadedAndCalculated.value = true
    }

    fun <T> changeMark(liveData: MutableLiveData<Boolean>, any:T?){
        var bool=false
        when(any){
            null->bool=false
            is Collection<*> ->{
                var temp =false
                for (position in any){
                    if (position!=null){temp=true}
                }
                bool=temp
            }
            is Array<*> -> if (any.isNotEmpty()){bool=true}
            is String -> if (any!="" || any!=null){bool=true}
            is Int -> if (any!=null) bool=true
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
            is Array<*> -> if (value.isNotEmpty()){return percent} else return 0
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