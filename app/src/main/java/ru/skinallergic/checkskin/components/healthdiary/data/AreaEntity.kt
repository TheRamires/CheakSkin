package ru.skinallergic.checkskin.components.healthdiary.data

import android.graphics.Bitmap
import java.io.File

data class AreaEntity (
        var kind: List<Int>? = null,
        var photos: MutableList<File?>?=null
        )