package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.ReminderRepository
import javax.inject.Inject

class RemindCommonViewModel @Inject constructor(val repo: ReminderRepository):BaseViewModel() {
    override var baseRepository: BaseHealthyRepository =repo

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }
    val remindLive = MutableLiveData<List<ReminderEntity?>>()
    val saveComplite = MutableLiveData<Boolean>()
    val redactComplete = MutableLiveData<Boolean>()
    val offRemind = MutableLiveData<Boolean>()
    val deletingComplete = MutableLiveData<Boolean>()

    fun getRemind(date: Long) : MutableLiveData<List<ReminderEntity?>> {
        compositeDisposable.add(repo.getRemind((date/1000).toString())
                ?.doOnSubscribe { splashScreenOn.set(true) }
                ?.doOnComplete { splashScreenOn.set(false) }
                ?.subscribe ({
                    remindLive.value=it
                },{
                }))
        return remindLive
    }

    fun newRemind( reminderWriter:ReminderWriter) :MutableLiveData<Boolean>{
        /*val rem=reminderWriter
        rem.start_at= rem.start_at?.div(1000)
        Loger.log("newRemind "+rem)*/
        compositeDisposable.add(
                repo.newRemind(reminderWriter)
                        .doOnSubscribe {progressBar.set(true)  }
                        .doOnComplete { progressBar.set(false) }
                        .subscribe ({
                            saveComplite.value=true
                            Loger.log(it.string()) },{})
        )

        return saveComplite
    }

    fun redactRemind(id: Int, reminderWriter : ReminderWriter) :MutableLiveData<Boolean>{
       /* val reminderWriter=ReminderWriter(
                start_at=date/1000 ,
                text =text,
                repeat_mode=repeatMode,
                kind=kind,
                remind=remind)*/
        repo.redactRemind(id,reminderWriter)
        return redactComplete
    }

    fun offRemind(id: Int,date: Long) :MutableLiveData<Boolean>{
        repo.offRemind(id,date/1000)
        return offRemind
    }

    fun deleteRemind(id: Int,date: Long) :MutableLiveData<Boolean>{
        compositeDisposable.add(
                repo.deleteRemind(id,(date/1000).toString())
                        ?.doOnSubscribe { progressBar.set(true) }
                        ?.doOnComplete { progressBar.set(false) }
                        ?.subscribe ({
                            deletingComplete.value=it
                            Loger.log("deleteRemind $it")
                        },{})
        )
        return deletingComplete
    }
}