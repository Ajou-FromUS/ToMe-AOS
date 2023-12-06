package data.service

import data.dto.response.MissionRecordResponse
import data.dto.response.UserResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface MyPageService {
    @PATCH("/user")
    suspend fun changeNickname(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken : String?,
        @Body requestBody : RequestBody
    ): UserResponse

    @GET("/user/mission/{yearMonth}")
    suspend fun getMonthlyMission(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken : String?,
        @Path("yearMonth") yearMonth: String
    ): MissionRecordResponse
}