package ru.skinallergic.checkskin.components.healthdiary.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.skinallergic.checkskin.components.news.pojo.Meta

data class BaseResponse <T>(
        @SerializedName("message")
        @Expose
        var message: String? = null,

        @SerializedName("meta")
        @Expose
        var meta: Meta? = null,

        @SerializedName("data")
        @Expose
        var data: T? = null,

        @SerializedName("errors")
        @Expose
        var errors: List<Any>? = null,

        @SerializedName("description")
        @Expose
        var description: String? = null

        )