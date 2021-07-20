package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.Loger
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
    val remindLive = MutableLiveData<String>()
    val gettingComplite = MutableLiveData<Boolean>()
    val redactComplete = MutableLiveData<Boolean>()
    val offRemind = MutableLiveData<Boolean>()
    val deletingComplete = MutableLiveData<Boolean>()

    fun getRemind(date: Long) : MutableLiveData<String> {
        repo.getRemind((date/1000).toString())
        return remindLive
    }

    fun newRemind( reminderWriter:ReminderWriter) :MutableLiveData<Boolean>{
        compositeDisposable.add(
                repo.newRemind(reminderWriter)
                        .doOnSubscribe {progressBar.set(true)  }
                        .doOnComplete { progressBar.set(false) }
                        .subscribe { Loger.log(it.string()) }
        )

        return gettingComplite
    }

    fun redactRemind(date: Long, text: String, repeatMode: Int, kind: Int, remind: Int) :MutableLiveData<Boolean>{
        val reminderWriter=ReminderWriter(
                start_at=date/1000 ,
                text =text,
                repeat_mode=repeatMode,
                kind=kind,
                remind=remind)
        repo.redactRemind(reminderWriter)
        return redactComplete
    }

    fun offRemind(date: Long) :MutableLiveData<Boolean>{
        repo.offRemind(date/1000)
        return offRemind
    }

    fun deleteRemind(date: Long) :MutableLiveData<Boolean>{
        repo.deleteRemind((date/1000).toString())
        return deletingComplete
    }
}