package ru.skinallergic.checkskin.entrance.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse (
        @SerializedName("message")
        @Expose
        val message:String?,

        @SerializedName("data")
        @Expose
        val data: Tokens?,

        @SerializedName("errors")
        @Expose
        val errors: List<Error>?,

        @SerializedName("description")
        @Expose
        val description: String
        )
