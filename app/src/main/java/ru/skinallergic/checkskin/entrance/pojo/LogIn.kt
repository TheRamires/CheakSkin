package ru.skinallergic.checkskin.entrance.pojo

import android.graphics.Region
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LogIn (
        @SerializedName("message")
        @Expose
        val message: String?,
        @SerializedName("data")
        @Expose
        val data: DataUser?,
        @SerializedName("errors")
        @Expose
        val errors: List<Error>?
        )

data class DataUser (
        @SerializedName("user")
        @Expose
        val user: User?,
        @SerializedName("tokens")
        @Expose
        val tokens: Tokens?
        )

data class Refresh (
        @SerializedName("value")
        @Expose
        val value:String?,
        @SerializedName("expire_at")
        @Expose
        val expireAt:Int?
        )

data class Tokens (
        @SerializedName("access")
        @Expose
        val access: Access?,
        @SerializedName("refresh")
        @Expose
        val refresh: Refresh?
        )

data class User (
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
        val tel: String?,
        @SerializedName("apple")
        @Expose
        val apple: String?,
        @SerializedName("google")
        @Expose
        val google: String?,
        @SerializedName("vk")
        @Expose
        val vk: String?,
        @SerializedName("facebook")
        @Expose
        val faceboock: String?,
        @SerializedName("region")
        @Expose
        val region: Region?,
        @SerializedName("diagnosis")
        @Expose
        val diagnosis: Diagnos?
        )

data class Access (
        @SerializedName("value")
        @Expose
        val value: String?,
        @SerializedName("expire_at")
        @Expose
        val expireAt:Int?
        )

