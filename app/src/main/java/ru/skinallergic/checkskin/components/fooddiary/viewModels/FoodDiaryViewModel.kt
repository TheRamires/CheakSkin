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
    val searchingDiaryList = MutableLiveData<List<FoodMealForMain?>>()

    fun getFoodDiaryByDate(date: Long) : MutableLiveData<List<FoodMealForMain?>>{
        Loger.log("getFoodDiaryByDate FoodDiaryViewModel")
        compositeDisposable.add(repository.getFoodDiaryByDate((date/1000).toString())
                ?.doOnSubscribe { splashScreenOn.set(true) }
                ?.doOnComplete { splashScreenOn.set(false) }
                ?.subscribe ({
                    Loger.log("//////// getFoodDiaryByDate + $it")
                    if(it.isEmpty()){foodDiaryList.value= listOf(); return@subscribe}

                    val created=it[0]?.created?:date
                    val setMeal: MutableSet<Int> = mutableSetOf()
                    val finalList = mutableListOf<FoodMealForMain>()
                    //val setCreatedData: MutableSet<Long> = mutableSetOf()
                    for(entity in it){
                        entity?.meal?.let { meal -> setMeal.add(meal) }
                        //entity?.created?.let { date -> setCreatedData.add(date) }

                    }
                    for (meal in setMeal){
                        val foodList= mutableListOf<FoodEntity>()
                        for (entity in it){
                            if (entity?.meal==meal){
                                val food = Food(entity.food?.name, entity.food!!.weight, entity.food.is_allergen)
                                val foodEntity = FoodEntity(entity.id, entity.created,entity.meal,food)
                                foodList.add(foodEntity)
                            }
                        }

                        finalList.add(FoodMealForMain(meal = meal, list = foodList,created = created))
                    }

                    foodDiaryList.value=finalList
                },{})
        )
        return foodDiaryList
    }

    fun getFoodDiarySearchByDate(date: Long, search: String): MutableLiveData<List<FoodMealForMain?>>{
        compositeDisposable.add(
                repository.getFoodDiarySearchByDate((date/1000).toString(),search)
                        ?.doOnSubscribe { splashScreenOn.set(true) }
                        ?.doOnComplete { splashScreenOn.set(false) }
                        ?.subscribe ({
                            val setDate: MutableSet<Long> = mutableSetOf()
                            val setMeal: MutableSet<Int> = mutableSetOf()
                            val finalList = mutableListOf<FoodMealForMain>()
                            for(entity in it){
                                entity?.created.let { date-> setDate.add(date!!) }
                                entity?.meal?.let { meal -> setMeal.add(meal) }
                                //entity?.created?.let { date -> setCreatedData.add(date) }

                            }
                            for (date in setDate){
                                for (meal in setMeal){
                                    val foodList= mutableListOf<FoodEntity>()
                                    for (entity in it){
                                        if (entity?.created==date && entity?.meal==meal){
                                            val food = Food(entity.food?.name, entity.food!!.weight, entity.food.is_allergen)
                                            val foodEntity = FoodEntity(entity.id, entity.created,entity.meal,food)
                                            foodList.add(foodEntity)
                                        }
                                    }
                                    if (foodList.isNotEmpty()){
                                        finalList.add(FoodMealForMain(meal = meal, list = foodList,created = date))
                                    }
                                }
                            }
                            searchingDiaryList.value=finalList

                        },{

                        })
        )
        return searchingDiaryList
    }
}