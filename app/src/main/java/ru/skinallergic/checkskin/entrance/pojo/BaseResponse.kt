package ru.skinallergic.checkskin.entrance.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse(
        @SerializedName("message")
        @Expose
        val message:String?,


        @SerializedName("errors")
        @Expose
        val error: List<Error>?,

        @SerializedName("data")
        @Expose
        val data: Datass?
)