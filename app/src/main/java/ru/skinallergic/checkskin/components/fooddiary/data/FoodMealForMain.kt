package ru.skinallergic.checkskin.components.fooddiary.data

import java.util.*


data class FoodMealForMain(
        val id: Int= Random().nextInt(10000),
        val meal: Int,
        val created: Long,
        val list: List<FoodEntity>
){
    fun getMealString(): String{
        var str=""
        when(meal){
            0-> str="Завтрак"
            1-> str="Обед"
            2-> str="Ужин"
            3-> str="Перекус"

        }
        return str
    }
}