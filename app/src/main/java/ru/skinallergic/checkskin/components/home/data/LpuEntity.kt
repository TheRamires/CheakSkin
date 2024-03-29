package ru.skinallergic.checkskin.components.home.data

data class LpuEntity(
        val name: String?="",
        val address : String?="",
        val phones : String?="",
        val lat: String? ="0.000",
        val lon: String? ="0.000",
        val rating : Double?=0.0
): BaseItem(){
    fun getRatingFloat(): Float{
        val final : Double =0.01*Math.floor(100*rating!!)
        return final.toFloat()
    }
}