package data.service

import data.dto.response.MissionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.time.LocalDate

interface MissionService {
    @GET("/user/mission/{today}")
    fun getMissions(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken : String?,
        @Path("today") today: String,
    ): Call<MissionResponse>
}