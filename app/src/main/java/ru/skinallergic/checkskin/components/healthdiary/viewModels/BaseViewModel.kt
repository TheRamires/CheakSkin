package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository

abstract class BaseViewModel: ViewModel() {
    val splashScreenOn = ObservableField(false);
    val compositeDisposable = CompositeDisposable()
    abstract var baseRepository : BaseHealthyRepository
    val expiredRefreshToken=MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        unsubscribe()
    }

    fun unsubscribe(){
        compositeDisposable.dispose()
        baseRepository.compositeDisposable.dispose()
    }
}
