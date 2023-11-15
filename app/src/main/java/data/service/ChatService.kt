package data.service

import data.dto.ChatDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatService {
    @POST("/chatbot")
    fun postMessage(
        @Header("AccessToken") accessToken: String?,
        @Header("RefreshToken") refreshToken : String?,
//        @Body chat : RequestBody): Call<ChatDTO>
    )
//    @GET ("/chatbot")
//    fun getMessage(): Call<ChatDTO>
}