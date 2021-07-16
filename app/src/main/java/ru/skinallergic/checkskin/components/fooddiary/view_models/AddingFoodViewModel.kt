package ru.skinallergic.checkskin.components.fooddiary.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.data.ProductPosition
import java.util.*
import javax.inject.Inject

class AddingFoodViewModel @Inject constructor(): ViewModel(){
    val viewList = MutableLiveData<MutableList<Int>>()
    val positionList = hashMapOf<Int, ProductPosition>()

    fun newId(): Int{
        val id= Random().nextInt(10000)
        Loger.log("newId "+id)

        val listId : MutableList<Int> = viewList.value?: mutableListOf()
        listId.add(id)
        viewList.value = listId
        newPosition(id)
        return  id
    }

    fun newPosition(id: Int){
        positionList[id]= ProductPosition(id)
    }

    fun addNameToMap(id: Int, name: String){

        var temp=positionList[id]
        if (temp!=null){
            println("temp •1")
            temp.name=name
        } else {
            println("temp •2")
            temp= ProductPosition(id,name=name)
        }
        positionList[id]=temp
    }
    fun addWeightToMap(id: Int, weight: String){

        var temp=positionList[id]
        if (temp!=null){
            temp.weight=weight
        } else {
            temp= ProductPosition(id,weight=weight)
        }
        positionList[id]=temp
    }
}