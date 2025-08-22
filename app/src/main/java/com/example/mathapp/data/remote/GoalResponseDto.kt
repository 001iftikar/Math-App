package com.example.mathapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoalResponseDto(
    val id: String,  // uuid → String
    @SerialName("created_at")
    val createdAt: String,  // timestamp with time zone → ISO 8601 string
    val title: String,
    val description: String,
    @SerialName("end_by")
    val endBy: String? = null,  // nullable timestamp → String?
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("user_id")
    val userId: String
)
