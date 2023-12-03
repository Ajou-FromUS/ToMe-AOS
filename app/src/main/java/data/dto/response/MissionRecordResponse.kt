package data.dto.response

import com.google.gson.annotations.SerializedName

data class MissionRecordResponse(
    val data: List<MissionData>

)
data class MissionData(
    @SerializedName("mission_id") val mid: Int,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("modified_at") val mDate: String,
    @SerializedName("created_at") val cDate: String
)