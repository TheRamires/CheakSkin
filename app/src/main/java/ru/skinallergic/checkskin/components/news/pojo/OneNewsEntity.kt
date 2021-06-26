package ru.skinallergic.checkskin.components.news.pojo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class OneNewsEntity {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("errors")
    @Expose
    var errors: List<Any>? = null

    @SerializedName("description")
    @Expose
    var description: String? = null
}
class Data {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("cover")
    @Expose
    var cover: String? = null

    @SerializedName("created")
    @Expose
    var created: Long? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    fun getDate():String{
        if (created==null){
            return ""
        }else {
            val date = created!! * 1000
            val formatter = SimpleDateFormat("dd.MM yyyy", Locale.ROOT)
            return formatter.format(date)
        }
    }
}