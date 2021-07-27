package ru.skinallergic.checkskin.components.fooddiary.data

import kotlin.random.Random

data class AllergicWriter(
        val id: Int = Random.nextInt(100000),
        var name: String,
        var alreadySaved: Boolean =false,
        var openRedacting: Boolean =false
){
    fun isFully():Boolean{
        return name!=""
    }
}