package data.dto.response

import com.google.gson.annotations.SerializedName

data class StatsResponse(
    val data: Data,

)
data class Data(
    @SerializedName("keyword_cloud_image_url")
    val keywordImg: String,

    @SerializedName("emotion_percentages")
    val emotionPercentages: EmotionPercentages,

    @SerializedName("completed_mission_counts")
    val completedMissionCounts: List<Int>,
)
data class EmotionPercentages(
    @SerializedName("neutral") val neutral: Float,
    @SerializedName("negative") val negative: Float,
    @SerializedName("positive") val positive: Float,
)