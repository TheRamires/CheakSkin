package ru.skinallergic.checkskin.components.fooddiary.data

import java.util.*

data class ProductEntity (
        val id: Int = Random().nextInt(100000),
        var name: String ="",
        var weight: String ="0",
        var isSavedOnServer: Boolean=false,
        var allergic: Boolean=false
        ){
    fun isFully():Boolean{
        return name!=""
    }
    fun getWeightString():String{
        if (weight=="0"){
            return ""
        } else return weight
    }
    fun notEmptyWeight():Boolean{
        return weight!="0"
    }
}