package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.components.fooddiary.repositories.FoodRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import javax.inject.Inject

class AllergenesViewModel @Inject constructor(val repository: FoodRepository): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }

    val isLoaded = MutableLiveData<Any>()
    val isAdded = MutableLiveData<Boolean>()
    val isDeleted = MutableLiveData<Boolean>()

    fun getAllergens(){
        repository.getAllergens(1) //Сделать пагинацию
    }
    fun addAllergens(name: String){
        repository.addAllergens(name)

    }
    fun deleteAllergens(id: Int){
        repository.deleteAllergens(id)

    }

}