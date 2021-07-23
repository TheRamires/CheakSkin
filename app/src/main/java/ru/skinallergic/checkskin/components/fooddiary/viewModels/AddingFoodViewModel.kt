package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWriter
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddingFoodViewModel @Inject constructor(): ViewModel(){
    val isBackSaved =MutableLiveData<Boolean>()

    fun conditionOfAdding(list: List<ProductEntity>): Boolean{
        if (list.isEmpty()){return true}
        val isFully=list[list.size-1].isFully()
        return isFully
    }
    fun conditionOfDelete(position: ProductEntity): Boolean{
        return position.isFully()
    }
    fun quitSaveCondition():Boolean{
        return true
    }
    fun backSave(list: List<ProductEntity>){
        println(list)
        isBackSaved.value=true
    }
    fun save(list: List<ProductEntity>){
        println(list)
    }
}