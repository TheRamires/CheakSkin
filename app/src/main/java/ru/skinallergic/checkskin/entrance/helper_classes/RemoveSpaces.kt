package ru.skinallergic.checkskin.entrance.helper_classes

fun removeSpaces(text: String):String {
    val temp = text.trim()
    val name=temp.replace("\\s+".toRegex(), " ")
    return name
}