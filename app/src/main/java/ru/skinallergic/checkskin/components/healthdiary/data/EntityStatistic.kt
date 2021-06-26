package ru.skinallergic.checkskin.components.healthdiary.data

import ru.skinallergic.checkskin.components.home.data.BaseItem

data class EntityStatistic (
        val status: Int?,
        val count: Int?
        ) : BaseItem()