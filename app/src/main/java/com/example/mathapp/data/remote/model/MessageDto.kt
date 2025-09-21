package com.example.mathapp.data.remote.model

import java.time.Instant

data class MessageDto(
    val groupId: String = "",
    val sender: String = "",
    val content: String = "",
    val createdAt: Long = Instant.now().toEpochMilli()
)
