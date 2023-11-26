package data.service

import data.dto.response.InitResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface InitClient {
    @GET("/")
    fun getInit(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken : String?,
    ): Call<InitResponse>
}