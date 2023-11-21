package data.service

import com.example.tome_aos.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    //server BASE_URL 추가
    private const val BASE_URL = BuildConfig.BASE_URL
    private var builder = OkHttpClient().newBuilder()
    private var okHttpClient = builder.build()

    private val retrofit = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val client = Retrofit.Builder()
        .client(retrofit)
        .baseUrl(BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApiClient() : Retrofit {
        return client
    }
}