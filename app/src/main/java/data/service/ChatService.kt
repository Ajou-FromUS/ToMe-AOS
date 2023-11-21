package data.service

import data.dto.response.ChatResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatService {
    @POST("/chatbot")
    fun postMessage(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken : String?,
        @Body requestBody : RequestBody
    ): Call<ChatResponse>
}