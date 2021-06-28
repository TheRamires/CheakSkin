package ru.skinallergic.checkskin.entrance.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("code")
        @Expose
        val code: Int?,
        @SerializedName("message")
        @Expose
        val message: String?
)