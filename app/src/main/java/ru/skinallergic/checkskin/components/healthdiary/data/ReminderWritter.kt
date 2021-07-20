package ru.skinallergic.checkskin.components.healthdiary.data

import ru.skinallergic.checkskin.components.healthdiary.components.reminders.*
import java.text.SimpleDateFormat
import java.util.*

data class ReminderWriter(
        var start_at: Long?,
        var text : String?,
        var repeat_mode: Int?,
        var kind: Int?,
        var remind: Int?
) {
    fun getTime():String{
        println("getTime $start_at")
        if (start_at!=null){
            val simpleTimeParser = SimpleDateFormat(TIME_PATTERN)
            return simpleTimeParser.format(Date(start_at!!))
        } else return ""
    }

    fun getRepeatMode(): String{
        when(repeat_mode){
            REPEAT_NEVER-> return "Никогда"
            REPEAT_EVERY_DAY-> return "Каждый день"
            REPEAT_EVERY_WEEK-> return "Каждую неделю"
            REPEAT_EVERY_TWO_WEEK-> return "Каждые 2 недели"
            REPEAT_EVERY_MONTH-> return "Каждый месяц"
            REPEAT_EVERY_YEAR-> return "Каждый год"
            else -> return ""

        }
    }
    fun getRemind(): String{
        when(remind){
            REMIND_IN_AN_NEVER->return "Никогда"
            REMIND_IN_AN_5_MIN->return "За 5 мин"
            REMIND_IN_AN_30_MIN->return "За 30 мин"
            REMIND_IN_AN_HOUR->return "За час"
            else-> return ""
        }
    }
}