package ru.skinallergic.checkskin.entrance.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Regions(
        @SerializedName("message")
        @Expose
        val message: String?,
        @SerializedName("data")
        @Expose
        val data: List<DatumR>?,
        @SerializedName("errors")
        @Expose
        val error: List<Error>?
        )
data class DatumR(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("name")
        @Expose
        val name: String?
)