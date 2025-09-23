package com.example.mathapp.mappers

import com.example.mathapp.data.remote.model.MessageDto
import com.example.mathapp.domain.model.Message
import com.example.mathapp.utils.sentMessageDateTime

fun MessageDto.toMessage(senderName: String): Message {
    return Message(
        senderId = sender,
        senderName = senderName,
        content = content,
        createdAt = createdAt.sentMessageDateTime(),
        status = status
    )
}