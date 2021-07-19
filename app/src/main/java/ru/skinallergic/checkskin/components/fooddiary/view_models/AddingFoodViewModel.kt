package ru.skinallergic.checkskin.components.fooddiary.view_models

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerProductAdapter_UNUSED
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddingFoodViewModel @Inject constructor(): ViewModel(){
    val productList = MutableLiveData<ArrayList<ProductEntity>>(arrayListOf())
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
    if (newList!=null && newList.contains(position)){
        newList.remove(position)
    }else {
        newList= arrayListOf(position)
    }
    this.value=newList
}