package ru.skinallergic.checkskin

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.type.Either
import ru.skinallergic.checkskin.type.Failure
import javax.inject.Inject

class Request @Inject constructor(val networkHandler: NetworkHandler){
    fun <T : Any> make(call: Call<T>): Either<Failure, T> {
        return when (networkHandler.isConnected) {
            true -> execute(call)
            false, null -> Either.Left(Failure.NetworkConnectionError())
        }
    }

    private fun <T : Any> execute(call: Call<T>): Either<Failure, T> {
        return try {

            val response = call.execute()

            when (response.code()) {
                200 ->return Either.Right(response.body()!!)
                401 -> {
                    val code=parserError(response.errorBody()?.string()!!)
                    if (code==4){
                        return Either.Left(Failure.AccessTokenExpired)
                    } else return Either.Left(Failure.ServerError("401"))
                }
                422 -> return  Either.Left(Failure.RefreshTokenExpired)
                else -> {
                    return Either.Left(Failure.ServerError(response.code().toString()))
                }
            }
            /* when(response.isSucceed()){
                 true->Either.Right(transform((response.body()!!)))
                 false-> Either.Left(Failure.ServerError(response.code().toString()))
             }*/
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError(exception.message.toString()))
        }
    }
}

fun <T : Any> Response<T>.isSucceed(): Boolean {
    return isSuccessful && body() != null && code()==200
}

fun parserError(errorBody: String): Int {
    Loger.log("PARSER BEGIN \n" + errorBody)
    val jObjError: JSONObject = JSONObject(errorBody)
    Loger.log("parser " + jObjError.toString())
    val jArray: JSONArray =jObjError.getJSONArray("errors")
    val jObject: JSONObject =jArray.getJSONObject(0)
    return jObject.get("code") as Int
}
