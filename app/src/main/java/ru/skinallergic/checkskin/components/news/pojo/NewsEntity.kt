package ru.skinallergic.checkskin.components.news.pojo
import ru.skinallergic.checkskin.components.home.data.BaseItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class NewsEntity {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("errors")
    @Expose
    var errors: List<Any>? = null

    @SerializedName("description")
    @Expose
    var description: String? = null
}
class Datum: BaseItem() {
        //id from BaseItem

        @SerializedName("created")
        @Expose
        var created: Long? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("text")
        @Expose
        var text: String? = null

        @SerializedName("cover")
        @Expose
        val cover: String? = null

    fun getDate():String{
        val date= created!! *1000
        val formatter = SimpleDateFormat("dd.MM yyyy", Locale.ROOT)
        return formatter.format(date)
    }
}

class Meta {
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null
}
class Pagination {
    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("has_prev")
    @Expose
    var hasPrev: Boolean? = null

    @SerializedName("has_next")
    @Expose
    var hasNext: Boolean? = null
}