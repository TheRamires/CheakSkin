package ru.skinallergic.checkskin.entrance.helper_classes

import ru.skinallergic.checkskin.Loger
import org.json.JSONArray
import org.json.JSONObject

fun parserError(errorBody: String): Int {
    Loger.log("PARSER BEGIN \n"+errorBody)
    val jObjError: JSONObject = JSONObject(errorBody)
    Loger.log("parser "+jObjError.toString())
    val jArray: JSONArray =jObjError.getJSONArray("errors")
    val jObject: JSONObject =jArray.getJSONObject(0)
    return jObject.get("code") as Int
}