package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.repositories.AffectedArreaRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.data.EntityAffected
import javax.inject.Inject

class AffectedAreaViewModel @Inject constructor(val repository: AffectedArreaRepository): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository =repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }
    var affectedLive1 = MutableLiveData<List<EntityAffected>?>()
    var affectedLive2 = MutableLiveData<List<EntityAffected>?>()
    val affectedLists: Unit
        get() {
            repository.loadAffectedsLists(affectedLive1, affectedLive2)
        }

    fun deletePosition(id: Int) {
        Loger.log("delete$id")
    }
    fun date(date:Long){
        compositeDisposable.add(
                repository.date((date/1000).toString())
                        .doOnSubscribe { splashScreenOn.set(true) }
                        .doOnComplete { splashScreenOn.set(false) }
                        .subscribe ({
                                    Loger.log("date $it")
                        },{})
        )
    }
}
