package com.iftikar.mathapp.mappers

import com.iftikar.mathapp.data.remote.model.MessageDto
import com.iftikar.mathapp.domain.model.Message
import com.iftikar.mathapp.utils.sentMessageDateTime

fun MessageDto.toMessage(senderName: String): Message {
    return Message(
        senderId = sender,
        senderName = senderName,
        content = content,
        createdAt = createdAt.sentMessageDateTime(),
        status = status
    )
}