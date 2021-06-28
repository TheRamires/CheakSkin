package ru.skinallergic.checkskin.components.healthdiary.remote

open class WritingData (
        val health_status: Int?=null,
        val topical_therapy: String?=null,
        val systemic_therapy:String?=null,
        val other_treatments:String?=null,
        val rating:Int?=null,
        val triggers: Array<Int>?=null
        ):Any()