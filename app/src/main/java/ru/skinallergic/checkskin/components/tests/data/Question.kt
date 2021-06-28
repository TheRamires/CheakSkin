package ru.skinallergic.checkskin.components.tests.data

data class Question(
        val id:Int,
        val text: String,
        val answer: List<Answer>
)

data class Answer(
        private val id: Int,
        val text:String,
        private val anotherWeight: Int=-1
){
    val weight: Int=id
            get() {
                if (anotherWeight>-1){return anotherWeight}
                else {return field}
            }
}

data class Result(
        val id: Int,
        val min:Int,
        val max:Int,
        val text:String,
        val description:String
)
