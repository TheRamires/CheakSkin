package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWr
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWriter
import ru.skinallergic.checkskin.components.fooddiary.repositories.FoodRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import javax.inject.Inject

class MealViewModel @Inject constructor(val repository: FoodRepository): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }
    val isLoaded = MutableLiveData<Any>()
    val isAdded = MutableLiveData<Boolean>()
    val isDeleted = MutableLiveData<Boolean>()

    fun addMeal(date : Long, meal:Int, name: String, weight: Int){
        val foodWr = FoodWr(name,weight)
        val foodWriter = FoodWriter(date/1000, meal,foodWr)
        repository.addMeal(foodWriter)
    }
    fun redactMeal(id: Int,date : Long, meal:Int, name: String, weight: Int){
        val foodWr = FoodWr(name,weight)
        val foodWriter = FoodWriter(date/1000, meal,foodWr)
        repository.redactMeal(id,foodWriter)
    }
    fun deleteMeal(id: Int){
        repository.deleteMeal(id)
    }
}