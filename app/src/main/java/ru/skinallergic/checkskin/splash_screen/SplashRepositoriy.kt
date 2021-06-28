package ru.skinallergic.checkskin.splash_screen

import ru.skinallergic.checkskin.Api.ApiService
import ru.skinallergic.checkskin.entrance.pojo.RefreshTokenResponse
import ru.skinallergic.checkskin.utils.REFRESH_TOKEN
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject


class SplashRepositoriy @Inject constructor(val apiService: ApiService) {

    fun refreshToken(refresh: String): Observable<Response<RefreshTokenResponse>> {
        val request = HashMap<String,String>()
        request[REFRESH_TOKEN] = refresh
        return apiService.refreshToken(request)
    }
}