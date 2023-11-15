package data.service

import com.example.tome_aos.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFuroClient {
    //server BASE_URL 추가
    private const val BASE_FURO = BuildConfig.BASE_FURO
    private var builder = OkHttpClient().newBuilder()
    private var okHttpClient = builder.build()

    private val client = Retrofit.Builder()
        .baseUrl(BASE_FURO).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApiClient() : Retrofit {
        return client
    }
}