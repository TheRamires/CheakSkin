package ru.skinallergic.checkskin.entrance.pojo

import com.google.gson.annotations.SerializedName

data class ProfileRequest (
        @SerializedName("name")
        var name: String?,
        @SerializedName("gender")
        var gender: Int?,
        @SerializedName("region")
        var region: Int?,
        @SerializedName("diagnosis")
        var diagnosis: Int?


        )