package ru.skinallergic.checkskin.Api

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWriter
import ru.skinallergic.checkskin.components.healthdiary.data.EntityStatistic
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter
import ru.skinallergic.checkskin.components.healthdiary.remote.BaseResponse
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.components.home.data.LpuEntity
import ru.skinallergic.checkskin.components.home.data.LpuOneEntity
import ru.skinallergic.checkskin.components.home.data.ReviewEntity
import ru.skinallergic.checkskin.components.home.data.ReviewWriter
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
    fun getData(@Header("Authorization") key: String, @Query("created") created: String): Observable<BaseResponse<GettingData?>>

    @GET("/journals/health/stat/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun statistic(@Header ("Authorization") key: String, @Query ("start") start: Long, @Query ("end") end: Long
    ): Observable<BaseResponse<List<EntityStatistic>>>

    @GET("/journals/health/stat/export/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun statisticPdf(@Header ("Authorization") key: String, @Query ("start") start: Long, @Query ("end") end: Long
    ): Observable<BaseResponse<Map<String, String>>>

    @Multipart
    @POST("/journals/health/rashes/")
    fun addRashReport(
            @Header("Authorization") key: String,
            //@Header("Content-Type") contentType: String,
                      @Query("created") created: Long,
                      @Part("area") area: RequestBody,
                      @Part("view") view: RequestBody,
                      @Part("kinds") kinds: RequestBody,
                      @Part vararg  file: MultipartBody.Part
    ):Observable<ResponseBody>


    @Multipart
    @PUT("/journals/health/rashes/{id}/")
    fun redactRashReport(
            @Header("Authorization") key: String,
            //@Header("Content-Type") contentType: String,
            @Path ("id") id:Int,
            @Part("area") area: RequestBody,
            @Part("view") view: RequestBody,
            @Part("kinds") kinds: RequestBody,
            @Part vararg  file: MultipartBody.Part
    ):Observable<ResponseBody>


    @DELETE("/journals/health/rashes/{id}/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun deletePosition(@Header("Authorization") key:String,@Path ("id") id:Int): Observable<ResponseBody>
}

interface NotificationService {
    @GET("/notifications/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getReminder(@Query("timestamp")date: String ,
                    @Header("Authorization") key: String,) :Observable<BaseResponse<List<ReminderEntity>>>

    @POST("/notifications/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun newReminder(@Header("Authorization") key: String, @Body reminderWriter: ReminderWriter): Observable<ResponseBody>

    @PATCH("/notifications/{id}/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun redactReminder(@Path("id") id: Int,@Header("Authorization") key: String, @Body reminderWriter: ReminderWriter): Observable<ResponseBody>

    @POST("/notifications/{id}/silent-list/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun offReminder(@Path("id") id: Int,@Header("Authorization") key: String, @Body timestamp: Map<String,Long>): Observable<ResponseBody>

    @DELETE("/notifications/{id}/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun deleteReminder(@Path("id") id: Int,@Query("timestamp")date: String ,@Header("Authorization") key: String): Observable<BaseResponse<Any>>

}

interface FoodService{
    @GET("/allergens/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getAllergens(@Query("page")page: Int, @Header("Authorization") key: String) : Observable<BaseResponse<List<AllergicEntity>>>

    @POST("/allergens/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun addAllergens(@Header("Authorization") key: String, @Body name: Map<String,String> ) : Observable<BaseResponse<Any>>

    @DELETE("/allergens/{id}/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun deleteAllergens(@Path("id") id: Int,@Header("Authorization") key: String) : Observable<ResponseBody>

    @GET("/journals/meal/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getFoodDiaryByDate(@Query("created")created: String, @Header("Authorization") key: String) : Observable<BaseResponse<List<FoodEntity?>>>

    @GET("/journals/meal/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getFoodDiarySearch(@Query("search")search: String, @Header("Authorization") key: String) : Observable<BaseResponse<List<FoodEntity?>>>

    @GET("/journals/meal/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getFoodDiarySearchByDate(@Query("created")created: String, @Query("search")search: String, @Header("Authorization") key: String) : Observable<BaseResponse<List<FoodEntity?>>>
    @GET("/journals/meal/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getFoodDiaryAll(@Header("Authorization") key: String) : Observable<ResponseBody>

    @POST("/journals/meal/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun addMeal(@Header("Authorization") key: String, @Body foodWrite: FoodWriter) : Observable<BaseResponse<Any>>

    @PATCH("/journals/meal/{id}/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun redactMeal(@Path("id") id: Int,@Header("Authorization") key: String, @Body foodWrite: FoodWriter): Observable<BaseResponse<Any>>

    @DELETE("/journals/meal/{id}/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun deleteMeal(@Path("id") id: Int,@Header("Authorization") key: String): Observable<BaseResponse<Any>>
}
interface DocService{
    @GET("/packages/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getDocs(): Observable<ResponseBody>

    @GET ("/packages/{package_id}/docs/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getDocPackage(@Path ("package_id") id: Int): Observable<ResponseBody>

    @GET("/docs/:doc_id/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun oneDoc(@Path ("doc_id") id: Int, @Query("fmt") fmt: String): Observable<ResponseBody>
}
interface LpuService{
    @GET("/health-facilities/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun allLpu(@Header("Authorization") key: String): Observable<BaseResponse<List<LpuEntity>>>

    @GET("/health-facilities/{id}/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun oneLpu(@Path ("id") id: Int,@Header("Authorization") key: String): Observable<BaseResponse<LpuOneEntity>>

    @GET("/health-facilities/favorites/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getFavorites(@Header("Authorization") key: String): Observable<BaseResponse<List<LpuEntity>>>

    @PUT("/health-facilities/{id}/is-favorite/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun addFavorite (@Path ("id") id: Int, @Header("Authorization") key: String, @Body is_favorite: Map<String,Boolean>): Observable<BaseResponse<Any>>

    @GET("/health-facilities/{id}/feedback/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun getReviews(@Path ("id") id: Int ,@Header("Authorization") key: String): Observable<BaseResponse<List<ReviewEntity>>>

    @POST("/health-facilities/{id}/feedback/")
    @Headers("Content-Type: application/json", "Connection: close")
    fun addReviews(@Path ("id") id: Int ,@Header("Authorization") key: String, @Body reviewWriter: ReviewWriter): Observable<BaseResponse<Any>>
}

