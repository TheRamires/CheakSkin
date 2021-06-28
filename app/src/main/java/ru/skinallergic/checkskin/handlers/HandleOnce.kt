package ru.skinallergic.checkskin.handlers

import javax.inject.Inject

class HandleOnce @Inject constructor () {
    private var lastMessage: String = ""

    fun itWasNotHandled(message: String): Boolean {
        return if (lastMessage != message) {
            lastMessage=message
            true
        } else false
    }
    fun clear(){
        lastMessage=""
    }
}