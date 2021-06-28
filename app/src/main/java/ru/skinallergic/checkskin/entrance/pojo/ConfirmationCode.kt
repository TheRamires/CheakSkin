package ru.skinallergic.checkskin.entrance.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ConfirmationCode(
        @SerializedName("message")
        @Expose
        val message: String?,

        @SerializedName("data")
        @Expose
        val data: DataCode?,

        @SerializedName("errors")
        @Expose
        val error: List<Error>?
)

data class DataCode(
        @SerializedName("code")
        @Expose
        val code: String?
)