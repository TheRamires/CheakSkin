package ru.skinallergic.checkskin.Api

import ru.skinallergic.checkskin.components.news.pojo.NewsEntity
import ru.skinallergic.checkskin.components.news.pojo.OneNewsEntity
import ru.skinallergic.checkskin.entrance.pojo.*
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.*
import java.io.IOException
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("/tel/validate/")
    fun cheakNumber(@Query("tel") number: String): Observable<ResultCheakNumber>

    @POST("/tel/verify/")
    @Headers("Content-Type: application/json")
    fun confirmationCode(@Body request: Map<String,String>): Observable<ConfirmationCode>

    @POST ("/siw/tel/")
    @Headers("Content-Type: application/json")
    fun loginRequest (@Header("Device") firebaseDeviceToken:String, @Body request: Map<String,String>): Observable<LogIn>

    @GET("/regions/")
    fun getRegions(): Observable<Regions>

    @GET("/diagnoses/")
    fun getDiagnosis(): Observable<Diagnosis>

    @PUT ("/users/self/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun redactProfile (@Header("Authorization") key:String, @Body profile: ProfileRequest
    ):Observable<Response<ProfileResponse>>

    @PUT ("/users/self/tel/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun redactNumber (@Header("Authorization") key:String, @Body redactNumberRequest: RedactNumberRequest
    ): Observable<Response<ProfileResponse>>

    @GET ("/users/self/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getProfile (@Header("Authorization") key:String): Observable<Response<ProfileResponse>>

    @DELETE ("/users/self/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun deleteAccount (@Header("Authorization") key:String): Observable<Response<ProfileResponse>>

    @POST("/tokens/refresh/")
    fun refreshToken (@Body refresh: Map<String, String>): Observable<Response<RefreshTokenResponse>>

    @GET("/sign-out/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun quit(@Header("Authorization") key:String, @Query ("total") value: Int
    ) :Observable<Response<ProfileResponse>>


    @POST("/siw/{Param}/")
    @Headers("Content-Type: application/json")
    fun netWorkLogIn(@Header("Device") firebaseDeviceToken:String, @Path ("Param") network:String, @Body token:  Map<String,String>): Observable<LogIn>


    @GET("/news/")
    fun getNewList(@Query("search") word: String,
                   @Query("keywords") keywords:String,
                   @Query("page") page:Int,
                   @Query("count") count:Int
    ) : Observable<NewsEntity>


    @GET("/news/{Param}/?fmt=md")
    fun getOneNews(@Path("Param") id:String): Observable<OneNewsEntity>

    companion object  {
        private const val BASE_URL = "http://192.168.0.119:5000"
        private const val BASE_URL_NEW ="http://217.25.94.101:5000/"

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                    //время ожидания
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)

                    .retryOnConnectionFailure(true)
                    .addInterceptor(logger)
                    .addNetworkInterceptor(object : Interceptor {  //против ошибки при getProfile : ava.net.ProtocolException: unexpected end of stream
                        @kotlin.Throws(IOException::class)
                        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                            val request: Request = chain.request().newBuilder()
                                    .addHeader("Connection", "close").build()
                            return chain.proceed(request)
                        }
                    })
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL_NEW)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiService::class.java)
        }
    }
}