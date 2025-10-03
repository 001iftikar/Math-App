package com.iftikar.mathapp.domain.model

import com.iftikar.mathapp.data.remote.model.MessageStatus

data class Message(
    val senderId: String,
    val senderName: String,
    val content: String,
    val createdAt: String,
    val status: MessageStatus
)
