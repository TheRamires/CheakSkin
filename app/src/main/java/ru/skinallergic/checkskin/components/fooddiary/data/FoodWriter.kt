package ru.skinallergic.checkskin.components.fooddiary.data

data class FoodWriter (
        val created : Long?,
        val meal : Int?,
        val food: FoodWr?
        )

data class FoodWr(
        val name : String?,
        val weight: Int=0
)
