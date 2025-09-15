package com.example.mathapp.data.remote.model

import com.example.mathapp.domain.model.Message
import io.ktor.http.ContentType
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String = "",
    val group_id: String,
    val sender: String,
    val content: String,
    val created_at: String = ""
)
