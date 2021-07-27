package ru.skinallergic.checkskin.components.home.data

class LpuOneEntity(
        val name: String?="",
        val address : String?="",
        val phones : String?="",
        val schedule: String?="",
        val lat: String? ="0.000",
        val lon: String? ="0.000",
        val htmc_available: Boolean?=true,
        val email: String?="",
        val url: String?="",
        val rating : Double?=0.0,
        val is_favorite : Boolean?=false
): BaseItem(){
    fun getRatingFloat(): Float{
        val final : Double =0.01*Math.floor(100*rating!!)
        return final.toFloat()
    }
}