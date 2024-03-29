package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.data.EntityStatistic
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.StatisticRepository
import ru.skinallergic.checkskin.handlers.ToastyManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class StatisticsViewModel @Inject constructor(
        val repository: StatisticRepository,
        val toastyManager: ToastyManager) : BaseViewModel(){
    override var baseRepository: BaseHealthyRepository =repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken

    }

    val formater= SimpleDateFormat("dd.MM.yyyy")

    val dateStart =MutableLiveData<Date>()
    val dateEnd =MutableLiveData<Date>()
    var mediatorLiveData = MediatorLiveData<Boolean>()

    val pdfUrl= MutableLiveData<String>()

    init {
        mediatorLiveData.addSource(dateStart, object : androidx.lifecycle.Observer<Date> {
            override fun onChanged(t: Date?) {
                if (t != null) {
                    mediatorLiveData.value = true
                }
            }
        })
        mediatorLiveData.addSource(dateEnd, object : androidx.lifecycle.Observer<Date> {
            override fun onChanged(t: Date?) {
                if (t != null) {
                    mediatorLiveData.value = true
                }
            }
        })
    }

    val statistic=MutableLiveData<List<EntityStatistic>>()


    fun getStart(): String{
        return formatingDate(formater, dateStart.value!!)
    }

    fun getEnd(): String{
        return formatingDate(formater, dateEnd.value!!)
    }

    fun formatingDate(foramter: SimpleDateFormat, date: Date):String{
        return foramter.format(date)
    }

    fun getStartUnix(): Long {
        return Objects.requireNonNull<Date>(dateStart.value).time/1000
    }

    fun getEndUnix(): Long {
        return Objects.requireNonNull<Date>(dateEnd.value).time/1000
    }

    fun statisticRequest(){
        Loger.log("getStartUnix " + getStartUnix())
        Loger.log("getEndUnix " + getEndUnix())
        compositeDisposable.add(repository.statistics(getStartUnix(), getEndUnix())
                .doOnSubscribe { splashScreenOn.set(true) }
                .doOnComplete { splashScreenOn.set(false) }
                .subscribe({

                    val entityStatistics: MutableList<EntityStatistic> = ArrayList()
                    for (entity in it) {
                        if (entity.count != 0) {
                            entityStatistics.add(entity)
                        }
                    }

                    statistic.value = entityStatistics
                }, { Loger.log("onError $it") }))
    }

    fun statisticPdfRequest(){
        Loger.log("getStartUnix " + getStartUnix())
        Loger.log("getEndUnix " + getEndUnix())
        compositeDisposable.add(repository.statisticsPdf(getStartUnix(), getEndUnix())
                .doOnSubscribe { progressBar.set(true) }
                .doOnComplete { progressBar.set(false) }
                .subscribe({
                    if (it!=""){
                        val url=it
                        Loger.log("statisticPdfRequest "+url)
                        pdfUrl.value=url
                    }
                }, { Loger.log("onError $it") }))
    }

    fun createStartDateLive(date: Date){
        val string: String=formatingDate(formater, date)
        val date: Date=simpleFormattingToDateWithMin(
                startWithMin(string)
        )
        dateStart.value=date
    }
    fun createEndDateLive(date: Date){
        val string: String=formatingDate(formater, date)
        val date: Date=simpleFormattingToDateWithMin(
                endWithMin(string)
        )
        dateEnd.value=date
    }

    fun startWithMin(date: String): String {
        return "00:00 $date"
    }

    fun endWithMin(date: String): String {
        return "23:59 $date"
    }

    fun simpleFormattingToDateWithMin(formatedStringDate: String?): Date {
        val formatter = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.US)
        var date: Date? = null
        try {
            date = formatter.parse(formatedStringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val unixTime = date!!.time / 1000
        return date
    }
}
