package ru.skinallergic.checkskin.Api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ServiceConfig {
    private const val isDebug: Boolean=true
    private const val BASE_URL ="http://217.25.94.101:5000/"   //OLD
    //private const val BASE_URL ="http://188.225.77.74:5000"  //NEW

    private fun makeServiceConfig(): OkHttpClient {
        return makeOkHttpClient(
                makeLoggingInterceptor((isDebug))
        )
    }

    private fun createGson(): Gson{
        return Gson()
    }

    fun makeService( ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(makeServiceConfig())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()

                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(object : Interceptor {  //против ошибки при getProfile : ava.net.ProtocolException: unexpected end of stream
                    @kotlin.Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                        val request: Request = chain.request().newBuilder()
                                .addHeader("Connection", "close").build()
                        return chain.proceed(request)
                    }
                })
                .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}