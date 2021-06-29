package ru.skinallergic.checkskin.components.healthdiary.remote

class GettingData (): WritingData(){
    val rashes: List<Rash>?=null
}
data class Rash(
        val id: Int,
        val area:Int?,
        val view:Int?,
        val kinds:List<Int>?,
        val photo_1: String?,
        val photo_2: String?,
        val photo_3: String?
)