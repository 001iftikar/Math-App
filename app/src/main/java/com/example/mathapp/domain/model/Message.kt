package com.example.mathapp.domain.model

data class Message(
    val senderId: String,
    val senderName: String,
    val content: String,
    val createdAt: String
)
