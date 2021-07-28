package ru.skinallergic.checkskin.view_models

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


fun dateStaticFormater(date: Date?): String? {
    val myDateFormatSymbols: DateFormatSymbols = object : DateFormatSymbols() {
        override fun getWeekdays(): Array<String> {
            return arrayOf("", "Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота")
        }
    }
    val sdf = SimpleDateFormat("EEEE, dd.MM.yyyy", myDateFormatSymbols)
    return sdf.format(date)
}