package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
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
    val foodDiaryList = MutableLiveData<List<FoodEntity?>>()

    fun getFoodDiaryByDate(date: Long) : MutableLiveData<List<FoodEntity?>>{
        Loger.log("getFoodDiaryByDate FoodDiaryViewModel")
        compositeDisposable.add(repository.getFoodDiaryByDate((date/1000).toString())
                ?.doOnSubscribe { splashScreenOn.set(true) }
                ?.doOnComplete { splashScreenOn.set(false) }
                ?.subscribe ({
                    foodDiaryList.value=it
                    Loger.log("getFoodDiaryByDate + $it")
                },{})
        )
        return foodDiaryList
    }

    fun getFoodDiarySearch(search: String){
        repository.getFoodDiarySearch(search)
    }

}