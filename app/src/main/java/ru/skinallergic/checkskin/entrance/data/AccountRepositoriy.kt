package ru.skinallergic.checkskin.entrance.data

import ru.skinallergic.checkskin.Api.ApiService
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.entrance.pojo.*
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.utils.REFRESH_TOKEN
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

class AccountRepositoriy @Inject constructor(val apiService: ApiService, val networkHandler: NetworkHandler,
val toastyManager: ToastyManager) {
    fun getRegions(): Observable<Regions> {
        networkHandler.check()
        return apiService.getRegions()
    }
    fun getDiagnosis(): Observable<Diagnosis>{
        networkHandler.check()
        return apiService.getDiagnosis()
    }
    fun redactProfile(key:String, profile: ProfileRequest):Observable<Response<ProfileResponse>> {
        networkHandler.check()
        return apiService.redactProfile(key, profile)
    }
    fun redactNumber(key:String, redactNumberRequest: RedactNumberRequest): Observable<Response<ProfileResponse>>{
        networkHandler.check()
        return apiService.redactNumber(key, redactNumberRequest)
    }

    fun getProfile(key: String): Observable<Response<ProfileResponse>>{
        networkHandler.check()
        return apiService.getProfile(key)
    }
    fun deleteAccount(key: String): Observable<Response<ProfileResponse>>{
        networkHandler.check()
        return apiService.deleteAccount(key)
    }
    fun refreshToken(refresh: String): Observable<Response<RefreshTokenResponse>>{
        networkHandler.check()
        val request = HashMap<String,String>()
        request[REFRESH_TOKEN] = refresh
        return apiService.refreshToken(request)
    }
    fun quit (key: String) :Observable<Response<ProfileResponse>>{
        networkHandler.check()
        val total: Int=1
        return apiService.quit(key, total)
    }
}