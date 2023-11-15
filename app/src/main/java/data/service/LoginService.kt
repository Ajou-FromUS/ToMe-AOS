package data.service

import com.example.tome_aos.BuildConfig
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Origin: ${BuildConfig.BASE_URL}")
    @POST("/sessions/code/authenticate")
    fun sendCode(@Body requestBody: RequestBody): Call<ResponseBody>
}