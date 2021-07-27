package ru.skinallergic.checkskin.components.home.data

data class ReviewEntity (
        val text: String,
        val vote : Int
): BaseItem(){
    fun getVoteFloat(): Float{
        return vote.toFloat()
    }
}