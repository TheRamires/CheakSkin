package ru.skinallergic.checkskin.entrance.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileResponse (
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
data class Datass(

        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("name")
        @Expose
        val name: String?,
        @SerializedName("gender")
        @Expose
        val gender: Int?,
        @SerializedName("tel")
        @Expose
        val tel: String?="",
        @SerializedName("apple")
        @Expose
        val apple :Int?,
        @SerializedName("google")
        @Expose
        val google: Long?,
        @SerializedName("vk")
        @Expose
        val vk: Long?,
        @SerializedName("facebook")
        @Expose
        val facebook: Long?,
        @SerializedName("region")
        @Expose
        val region: Regionas?,
        @SerializedName("diagnosis")
        @Expose
        val diagnosis: Diagnos?
)

data class Diagnos (

        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("name")
        @Expose
        val name: String?
)
data class Regionas (
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("name")
        @Expose
        val name: String?
)