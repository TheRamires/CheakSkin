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

        @SerializedName("pagination")
        @Expose
        var pagination: Pagination? = null,

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
data class Pagination(

        @SerializedName("page")
        @Expose
        val page: Int,

        @SerializedName("total")
        @Expose
        val total: Int,

        @SerializedName("has_prev")
        @Expose
        val has_prev: Boolean,

        @SerializedName("has_next")
        @Expose
        val has_next: Boolean
)