package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.components.fooddiary.data.Food
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWr
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWriter
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.repositories.FoodRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import ru.skinallergic.checkskin.handlers.ToastyManager
import javax.inject.Inject

class MealViewModel @Inject constructor(
        val repository: FoodRepository,
        val toastyManager: ToastyManager
        ): BaseViewModel() {
    val productList = MutableLiveData<ArrayList<ProductEntity>>(arrayListOf())
    val savedIdTemp = listOf<Int>()

    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }

    val isLoaded = MutableLiveData<Any>()
    val isBackSaved = MutableLiveData<Boolean>()

    val isSaved=MutableLiveData<Boolean>()
    val isRedacted=MutableLiveData<Boolean>()
    val isDeleted = MutableLiveData<Boolean>()

    var meal: Int?=null

    fun saveCondition(): Boolean{
        if (meal==null){
            toastyManager.toastyyyy("Выберите прием пищи", true)
            return false
        } else return true
    }
    fun quitSaveCondition():Boolean{
        if (productList.value==null || productList.value?.isEmpty() == true || hasSavedPosition()){
            return false
        } else return true
    }
    private fun hasSavedPosition():Boolean{
        var boolean=false
        val list=productList.value
        if (list != null) {
            for (product in list){
                if (product.isSavedOnServer){
                    boolean=true
                }
            }
        }
        return boolean
    }

    fun addMealsAndConvert(date: Long, meal: Int, productList: List<ProductEntity>, backSaved: MutableLiveData<Boolean>?=null){
        println("addMealsAndConvert")
        for (product in productList){
            if(product.isSavedOnServer){continue}
            val foodWr=FoodWr(
                    product.name,
                    product.weight?.toInt()
            )
            var finalWeight : Int?=null
            if (product.weight!=null){finalWeight= product.weight!!.toInt()}
            addMeal(product.id, date, meal, product.name,finalWeight, backSaved)
        }
    }

    fun addMeal(internalId:Int, date : Long, meal:Int, name: String?, weight: Int?, backSaved: MutableLiveData<Boolean>?){
        println("addMeal")
        val finalWeight=weight?:0
        val foodWr = FoodWr(name,finalWeight)
        val foodWriter = FoodWriter(date/1000, meal,foodWr)
        compositeDisposable.add(
                repository.addMeal(foodWriter)
                        .doOnSubscribe { progressBar.set(true) }
                        .doOnComplete { progressBar.set(false) }
                        .subscribe ({
                            if (it=="Ok"){
                                productList.markSavedPosition(internalId)
                                backSaved?.let { backSaved.value=true }
                                isSaved.value=true
                            }
                        },{})
        )
    }
    fun redactMealAndConvert(date: Long, meal: Int, list: List<ProductEntity>){
        for (entity in list){
            redactMeal(entity.id,date,meal, entity.name,entity.weight.toInt())
        }
    }

    fun redactMeal(id: Int,date : Long, meal:Int, name: String, weight: Int){
        val foodWr = FoodWr(name,weight)
        val foodWriter = FoodWriter(date/1000, meal,foodWr)
        compositeDisposable.add(
                repository.redactMeal(id,foodWriter)
                        .doOnSubscribe { progressBar.set(true) }
                        .doOnComplete { progressBar.set(false) }
                        .subscribe ({
                            if (it=="Ok"){
                                isRedacted.value=true
                            }
                        },{})
        )
    }
    fun deleteMeal(idList: List<Int>){
        for (id in idList){
            compositeDisposable.add(
                    repository.deleteMeal(id)
                            .doOnSubscribe { progressBar.set(true) }
                            .doOnComplete { progressBar.set(false) }
                            .subscribe ({
                                if (it=="Ok"){
                                    isDeleted.value=true
                                }
                            },{})
            )
        }
    }
    fun deleteMeal(id: Int){
        compositeDisposable.add(
                repository.deleteMeal(id)
                        .doOnSubscribe { progressBar.set(true) }
                        .doOnComplete { progressBar.set(false) }
                        .subscribe ({
                            if (it=="Ok"){
                                isDeleted.value=true
                            }
                        },{})
        )
    }
}
fun <T>MutableLiveData<ArrayList<T>>.add(position: T){
    var newList  =this.value
    if (newList!=null){
        newList.add(position)
    }else {
        newList= arrayListOf(position)
    }
    this.value=newList
}
fun <T>MutableLiveData<ArrayList<T>>.delete(position: T){
    var newList  =this.value
    println("newList $newList, posotopn $position")
    if (newList!=null && newList.contains(position)){
        newList.remove(position)
    }else {
        newList= arrayListOf(position)
    }
    this.value=newList
}
fun MutableLiveData<ArrayList<ProductEntity>>.markSavedPosition(id: Int){
    var newList  =this.value
    if (newList != null) {
        for (position in newList){
            if (position.id==id){
                position.isSavedOnServer=true
            }
        }
    }
    this.value=newList
}