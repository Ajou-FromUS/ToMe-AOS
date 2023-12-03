package data.service

import data.dto.response.UserResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

interface MyPageService {
    @PATCH("/user")
    suspend fun changeNickname(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken : String?,
        @Body requestBody : RequestBody
    ): UserResponse
}