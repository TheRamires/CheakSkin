package ru.skinallergic.checkskin.components.fooddiary.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerProductAdapter
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddingFoodViewModel @Inject constructor(): ViewModel(){
    val productList = MutableLiveData<ArrayList<ProductEntity>>(arrayListOf())

    fun addProduct(adapter: RecyclerProductAdapter){
        val newList  = productList.value
        Loger.log("adapter.getList 1.2 "+adapter.getDataList())
        newList?.add(ProductEntity())
        Loger.log("adapter.getList 1.3 "+adapter.getDataList())
        productList.value= newList!!
        Loger.log("adapter.getList 1.4 "+adapter.getDataList())
        println("productList ${productList.value}")
    }
    fun deletePosition(position: ProductEntity){
        val newList  = productList.value
        newList?.remove(position)
        productList.value= newList!!

    }
}