package data.service

import com.example.tome_aos.BuildConfig
import data.dto.response.JWTTokenResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Origin: ${BuildConfig.REDIRECT_URL}")
    @POST("/sessions/code/authenticate")
    fun sendCode(@Body requestBody: RequestBody): Call<JWTTokenResponse>


}