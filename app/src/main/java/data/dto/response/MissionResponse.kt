package data.dto.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class MissionResponse (
    @SerializedName("data") val missionsData: List<Data>,
){
    data class Data(
        @SerializedName("mission_id") val mission_id: Int?,
        @SerializedName("uid") val uid: Int?,
        @SerializedName("is_completed") val is_completed: Boolean?,
        @SerializedName("id") val id: Int?,
        @SerializedName("content") val content: String?,
        @SerializedName("mission") val mission: Mission?,
    ) : Serializable {
        data class Mission(
            @SerializedName("id") val id: Int?,
            @SerializedName("type") val type: Int?,
            @SerializedName("title") val title: String?,
            @SerializedName("content") val content: String?,
        )
    }
}