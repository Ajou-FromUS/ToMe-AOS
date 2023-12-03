package data.dto.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("status_code") val stateCode: Int,
    val data: UserData
)
data class UserData(
    @SerializedName("nickname") val nickname: String
)
