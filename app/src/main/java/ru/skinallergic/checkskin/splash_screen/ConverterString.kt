package ru.skinallergic.checkskin.splash_screen

import ru.skinallergic.checkskin.components.news.pojo.Datum
import ru.skinallergic.checkskin.entrance.pojo.Datass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ConverterString {

    var gson = Gson()

    fun stringToNews(data: String?): List<Datum?> {
        if (data == null) {
            return emptyList<Datum?>()
        }
        val listType = object : TypeToken<List<Datum?>>() {}.type
        return gson.fromJson<List<Datum?>>(data, listType)
    }

    fun newsToString(someObjects: List<Datum?>): String? {
        return gson.toJson(someObjects)
    }
    //PROFILE--------------------------------------------------------

    fun profileToString(some: Datass): String?{
        val json= gson.toJson(some)
        return json
    }
    fun stringToProfile(data: String?): Datass?{
        if (data == null) {
            return null
        }
        val listType = object : TypeToken<Datass>() {}.type
        return gson.fromJson<Datass>(data, listType)
    }
}
