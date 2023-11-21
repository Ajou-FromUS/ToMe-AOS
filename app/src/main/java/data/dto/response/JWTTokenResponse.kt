package data.dto.response

import com.google.gson.annotations.SerializedName

data class JWTTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)
