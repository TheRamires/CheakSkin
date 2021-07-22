package ru.skinallergic.checkskin.components.fooddiary.data

data class FoodEntity(
        val id: Int,
        val created : Long?,
        val meal : Int?,
        val food: Food?
)

data class Food(
        val name : String?,
        val weight: Int=0,
        val is_allergen : Boolean=false
)