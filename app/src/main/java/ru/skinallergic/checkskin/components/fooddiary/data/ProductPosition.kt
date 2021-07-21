package ru.skinallergic.checkskin.components.fooddiary.data

import java.util.*

data class ProductEntity (
        val id: Int = Random().nextInt(100000),
        var name: String ="",
        var weight: String =""
        ){
    fun isFully():Boolean{
        return name!=""
    }
}