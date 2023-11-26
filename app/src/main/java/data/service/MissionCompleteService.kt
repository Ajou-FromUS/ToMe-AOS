package data.service

import data.dto.response.MissionResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MissionCompleteService {
    @PATCH("/user/mission/{mission_id}")
    fun getMissions(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken: String?,
        @Path("mission_id") missionID: Int?,
        @Body requestBody: RequestBody
    ): Call<MissionResponse.Data>
}