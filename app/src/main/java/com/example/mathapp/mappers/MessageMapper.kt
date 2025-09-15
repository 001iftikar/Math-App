package com.example.mathapp.mappers

import com.example.mathapp.data.remote.model.MessageDto
import com.example.mathapp.domain.model.Message

fun MessageDto.toMessage(): Message {
    return Message(
        sender = sender,
        content = content,
        createdAt = created_at
    )
}