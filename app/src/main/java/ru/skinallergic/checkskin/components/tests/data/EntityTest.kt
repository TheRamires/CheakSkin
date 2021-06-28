package ru.skinallergic.checkskin.components.tests.data

import org.w3c.dom.ProcessingInstruction
import ru.skinallergic.checkskin.components.home.data.BaseItem

data class EntityTest (
        val id: Long,
        val name:String,
        val description:String,
        val instruction: String,
        val image:Int,
        val questions: List<Question>,
        var sum: Int =0,
        var passingTime: Int =5,
        val results: List<Result>
        ): BaseItem()
