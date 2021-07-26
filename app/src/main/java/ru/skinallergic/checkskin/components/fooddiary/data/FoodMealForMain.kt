package ru.skinallergic.checkskin.components.fooddiary.data

import java.util.*


data class FoodMealForMain(
        val id: Int= Random().nextInt(10000),
        val meal: Int,
        val created: Long,
        val list: List<FoodEntity>
)