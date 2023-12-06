package data.dto.response

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("message") val message: String,
    @SerializedName("mission_count") val mission_count: Int,
    val userFlage: Boolean
)
