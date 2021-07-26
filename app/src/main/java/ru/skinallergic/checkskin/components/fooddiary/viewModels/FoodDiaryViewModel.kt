package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.data.Food
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodMealForMain
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
    val foodDiaryList = MutableLiveData<List<FoodMealForMain?>>()

    fun getFoodDiaryByDate(date: Long) : MutableLiveData<List<FoodMealForMain?>>{
        Loger.log("getFoodDiaryByDate FoodDiaryViewModel")
        compositeDisposable.add(repository.getFoodDiaryByDate((date/1000).toString())
                ?.doOnSubscribe { splashScreenOn.set(true) }
                ?.doOnComplete { splashScreenOn.set(false) }
                ?.subscribe ({
                    val created=it[0]?.created?:date
                    val setMeal: MutableSet<Int> = mutableSetOf()
                    val finalList = mutableListOf<FoodMealForMain>()
                    //val setCreatedData: MutableSet<Long> = mutableSetOf()
                    for(entity in it){
                        entity?.meal?.let { meal -> setMeal.add(meal) }
                        //entity?.created?.let { date -> setCreatedData.add(date) }

                    }
                    for (meal in setMeal){
                        val foodList= mutableListOf<Food>()
                        for (entity in it){
                            if (entity?.meal==meal){
                                foodList.add(Food(entity.food?.name, entity.food!!.weight, entity.food.is_allergen))
                            }
                        }

                        finalList.add(FoodMealForMain(meal = meal, list = foodList,created = created))
                    }

                    foodDiaryList.value=finalList
                    Loger.log("getFoodDiaryByDate + $it")
                },{})
        )
        return foodDiaryList
    }

    fun getFoodDiarySearch(search: String){
        repository.getFoodDiarySearch(search)
    }

}