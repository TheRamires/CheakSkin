package ru.skinallergic.checkskin.components.healthdiary.remote

class GettingData (): WritingData(){
    val rashes: List<Rash>?=null
}
data class Rash(
        val id: Int,
        val area:Int?,
        val view:Int?,
        val kinds:List<Int>?,
        var photo_1: String?,
        var photo_2: String?,
        var photo_3: String?
)