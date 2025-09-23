package com.example.mathapp.domain.model

import com.example.mathapp.data.remote.model.MessageStatus

data class Message(
    val senderId: String,
    val senderName: String,
    val content: String,
    val createdAt: String,
    val status: MessageStatus
)
