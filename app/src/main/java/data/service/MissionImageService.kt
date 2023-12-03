package data.service

import data.dto.response.MissionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface MissionImageService {
    @Multipart
    @PATCH("/user/mission/{mission_id}")
    fun patchImageMissions(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken: String?,
        @Path("mission_id") missionID: Int?,
        @Part missionImage: MultipartBody.Part
    ): Call<MissionResponse.Data>
}