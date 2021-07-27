package ru.skinallergic.checkskin.components.fooddiary.data

data class FoodEntity(
        var id: Int,
        val created : Long?,
        val meal : Int?,
        val food: Food?
)

data class Food(
        val name : String?,
        val weight: Int=0,
        val is_allergen : Boolean=false
){
    fun notEmptyWeight():Boolean{
        return weight>0
    }
    fun getWeightString():String{
        if (weight>0){
            return weight.toString()
        } else return ""
    }
}