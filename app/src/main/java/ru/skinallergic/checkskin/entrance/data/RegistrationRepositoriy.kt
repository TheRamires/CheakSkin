package ru.skinallergic.checkskin.entrance.data

import ru.skinallergic.checkskin.Api.ApiService
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.entrance.pojo.ConfirmationCode
import ru.skinallergic.checkskin.entrance.pojo.LogIn
import ru.skinallergic.checkskin.entrance.pojo.ResultCheakNumber
import io.reactivex.Observable
import javax.inject.Inject
private const val TEL_KEY="tel"
private const val CODE_KEY="code"
const val TOKEN="token"

class RegistrationRepositoriy  @Inject constructor (val apiService: ApiService, val networkHandler: NetworkHandler){
     fun checkNumber(number: String): Observable<ResultCheakNumber>  {
         networkHandler.check()
         return apiService.cheakNumber(number)
    }
    fun sendingCode(number: String): Observable<ConfirmationCode>{
        networkHandler.check()
        val request = HashMap<String,String>()
        request[TEL_KEY] = number
        return apiService.confirmationCode(request)
    }
    fun loginRequest(number: String, code: String): Observable<LogIn>{
        networkHandler.check()
        val request = HashMap<String,String>()
        request[TEL_KEY] = number
        request[CODE_KEY] = code
        return apiService.loginRequest(request)
    }
    fun netWorklogIn(network: String, token: String): Observable<LogIn>{
        networkHandler.check()
        val request = HashMap<String,String>()
        request[TOKEN]=token
        return apiService.netWorkLogIn(network,request)
    }
}