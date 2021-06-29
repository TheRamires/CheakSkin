package ru.skinallergic.checkskin.components.healthdiary

import ru.skinallergic.checkskin.R

object AreaManager {
    val head: Int=R.id.toggle_head
    val hand01: Int=R.id.toggle_hand_1
    val hand02: Int=R.id.toggle_hand_2
    val bodyCenter: Int=R.id.toggle_body_center
    val leg01: Int=R.id.toggle_leg_1
    val leg02: Int=R.id.toggle_leg_2

    const val HEAD=0
    const val HAND_01=1
    const val HAND_02=2
    const val BODY_CENTER=3
    const val LEG_01=4
    const val LEG_02=5

    fun getViewTitle(view: Int):String{
        var text=""
        when(view){
            0 -> text = "(Спереди)"
            1 -> text = "(Сзади)"
        }
        return text
    }

    fun getTitle(id: Int, view: Int):String{
        var text=""
        when (id) {
            0 -> text = "Голова"
            1 -> if (view == 0) {
                text = "Правая рука"
            } else text = "Левая рука"
            2 -> if (view == 0) {
                text = "Левая рука"
            } else text = "Правая рука"
            3 -> text = "Тело"
            4 -> if (view == 0) {
                text = "Правая нога"
            } else text = "Левая нога"
            5 -> if (view == 0) {
                text = "Левая нога"
            } else text = "Правая нога"
        }
        return text
    }
    fun getIdOfArea(id: Int):Int?{
        var area: Int?=null
        when (id) {
            head -> area = HEAD
            hand01 -> area = HAND_01
            hand02 -> area = HAND_02
            bodyCenter -> area = BODY_CENTER
            leg01 -> area = LEG_01
            leg02 -> area = LEG_02
        }
        return area
    }
    fun getIdOfToggle(id: Int):Int?{
        var idOfToggle: Int?=null
        when(id){
            HEAD -> idOfToggle = head
            HAND_01 -> idOfToggle = hand01
            HAND_02 -> idOfToggle = hand02
            BODY_CENTER -> idOfToggle = bodyCenter
            LEG_01 -> idOfToggle = leg01
            LEG_02 -> idOfToggle = leg02
        }
        return idOfToggle
    }
    fun getKindFromButtonId(buttonId: Int): Int?{
        var kind: Int? =null
        when (buttonId) {
            R.id.toggle_0 -> kind = 0
            R.id.toggle_1 -> kind = 1
            R.id.toggle_2 -> kind = 2
            R.id.toggle_3 -> kind = 3
            R.id.toggle_4 -> kind = 4
            R.id.toggle_5 -> kind = 5
        }
        return kind
    }
    fun getKindTitle(index: Int): String?{
        return when(index){
            0->"Крупная сыпь"
            1->"Мелкая сыпь"
            2->"Трещины"
            3->"Сухость"
            4->"Отёк"
            5->"Эрозия"
            else -> null
        }
    }
}