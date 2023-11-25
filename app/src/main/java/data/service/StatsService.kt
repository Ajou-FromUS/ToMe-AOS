package data.service

import data.dto.response.StatsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StatsService {
    @GET("/statistics/{yearMonth}")
    suspend fun getStatisticsForYearMonth(
        @Header("access_token") accessToken: String?,
        @Header("refresh_token") refreshToken : String?,
        @Path("yearMonth") yearMonth: String
    ): StatsResponse
}