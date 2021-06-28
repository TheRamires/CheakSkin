package ru.skinallergic.checkskin.components.tests.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.skinallergic.checkskin.components.news.pojo.Datum
import ru.skinallergic.checkskin.components.news.pojo.Meta
import java.text.SimpleDateFormat


data class TestResponse(
        @SerializedName("message")
        @Expose
        val message: String? = null,

        @SerializedName("meta")
        @Expose
        val meta: Meta? = null,

        @SerializedName("data")
        @Expose
        val data: List<TestResult>? = null,

        @SerializedName("errors")
        @Expose
        val errors: List<Any>? = null,

        @SerializedName("description")
        @Expose
        val description: String? = null

)

data class TestResult(
        @SerializedName("created")
        @Expose
        val created: Long? = null,

        @SerializedName("result")
        @Expose
        val result: String? = null
){

        val safeDate: String
                get() :String {
                        val sdf = SimpleDateFormat("dd.MM.yyyy")
                        return sdf.format(created!!*1000)
                }
}