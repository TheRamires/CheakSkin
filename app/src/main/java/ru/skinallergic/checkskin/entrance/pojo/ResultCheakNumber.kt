package ru.skinallergic.checkskin.entrance.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultCheakNumber(
        @SerializedName("data")
        @Expose
        val data: Data?
)
data class Data(
        @SerializedName("success")
        @Expose
        val success: Boolean?
)