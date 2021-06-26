package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.HealthyDiaryRepository
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import javax.inject.Inject

class TriggersViewModel @Inject constructor(val repository: HealthyDiaryRepository) : BaseViewModel() {

    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }

    val loaded= MutableLiveData<GettingData?>()

    fun getData(date: Long){
        Loger.log("getDATA")
        repository.getData((date/1000).toString(),loaded,splashScreenOn)
    }
}
