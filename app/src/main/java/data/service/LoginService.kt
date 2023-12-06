package data.service

import com.example.tome_aos.BuildConfig
import data.dto.response.JWTTokenResponse
import data.dto.response.UserResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Origin: ${BuildConfig.REDIRECT_URL}")
    @POST("/sessions/code/authenticate")
    fun sendCode(@Body requestBody: RequestBody): Call<JWTTokenResponse>


    @POST("/user")
    fun createUser(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken : String?,
        @Body requestBody : RequestBody
    ): Call<UserResponse>
}