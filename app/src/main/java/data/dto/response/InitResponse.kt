package data.dto.response

import com.google.gson.annotations.SerializedName

data class InitResponse(
    @SerializedName("data") val initData: Data
) {
    data class Data(
        @SerializedName("nickname") val nickname: String,
        @SerializedName("has_mission_today") val has_mission_today: Boolean,
    )
}

