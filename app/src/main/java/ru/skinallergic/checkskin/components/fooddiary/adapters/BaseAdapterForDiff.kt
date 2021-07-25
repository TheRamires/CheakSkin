package ru.skinallergic.checkskin.components.fooddiary.adapters

import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity

interface BaseAdapterForDiff<T> {
    abstract var list: ArrayList<T>
    fun addData( myData: T)
    fun removeData( myData: T)
}