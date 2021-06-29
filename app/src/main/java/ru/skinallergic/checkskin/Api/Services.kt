package ru.skinallergic.checkskin.Api

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.skinallergic.checkskin.components.healthdiary.data.EntityStatistic
import ru.skinallergic.checkskin.components.healthdiary.remote.BaseResponse
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.components.tests.data.PostResponse
import ru.skinallergic.checkskin.components.tests.data.TestResponse
import ru.skinallergic.checkskin.entrance.pojo.RefreshTokenResponse
import java.io.File

interface HomeService{
    @GET("/regions/")
    fun getLpuList(): Call<List<Any>>
}

interface TestResultService {
    @POST("/tests/{Param}/results/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun sendResult(@Header("Authorization") key: String, @Path("Param") test: String, @Body result: Map<String, String>) : Observable<PostResponse>

    @GET("/tests/{Param}/results/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getResult(@Header("Authorization") key: String, @Path("Param") test: String) : Observable<TestResponse>

    @POST("/tokens/refresh/")
    fun refreshToken(@Body refresh: Map<String, String>): Observable<RefreshTokenResponse>
}
//----------------Заменить на отедльынй интерфейс
interface TokenService{
    @POST("/tokens/refresh/")
    fun refreshToken(@Body refresh: Map<String, String>): Observable<RefreshTokenResponse>
}

interface HealthyService {
    @PUT("/journals/health/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun redact(@Header("Authorization") key: String, @Query("created") created: String, @Body writingData: WritingData?): Observable<BaseResponse<WritingData>>

    @GET("/journals/health/?")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getData(@Header("Authorization") key: String, @Query("created") created: String): Observable<BaseResponse<GettingData>>


    @GET("/journals/health/stat/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun statistic(@Header ("Authorization") key: String, @Query ("start") start: Long, @Query ("end") end: Long
    ): Observable<BaseResponse<List<EntityStatistic>>>


    @Multipart
    @POST("/journals/health/rashes/")
    fun addRashReport(
            @Header("Authorization") key: String,
            //@Header("Content-Type") contentType: String,
                      @Query("created") created: Long,
                      @Part("area") area: RequestBody,
                      @Part("view") view: RequestBody,
                      @Part("kind") kind: RequestBody,
                      @Part vararg  file: MultipartBody.Part
                     /* @Part file1: MultipartBody.Part,
                      @Part file2: MultipartBody.Part,
                      @Part file3: MultipartBody.Part*/
    ):Observable<ResponseBody>

    @Multipart
    @PUT("/journals/health/rashes/{id}/")
    fun redactRashReport(
            @Header("Authorization") key: String,
            //@Header("Content-Type") contentType: String,
            @Path ("id") id:Int,
            @Part("area") area: RequestBody,
            @Part("view") view: RequestBody,
            @Part("kind") kind: RequestBody,
            @Part vararg  file: MultipartBody.Part
    ):Observable<ResponseBody>
}

