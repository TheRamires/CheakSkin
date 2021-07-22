package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.components.fooddiary.repositories.FoodRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import javax.inject.Inject

class FoodDiaryViewModel @Inject constructor(val repository: FoodRepository): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }
    val foodDiaryList = MutableLiveData<List<Any>>()

    fun getFoodDiaryByDate(date: Long){
        repository.getFoodDiaryByDate((date/1000).toString())
    }
    fun getFoodDiarySearch(search: String){
        repository.getFoodDiarySearch(search)
    }

}