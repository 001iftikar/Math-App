package com.example.mathapp.presentation.goal.shared_goals.chat_screen

import com.example.mathapp.domain.model.Message

data class ChatScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val messages: List<Message> = emptyList(),
    val currentUserId: String? = null,
    val textMessage: String = "",
    val groupName: String,
    val groupId: String
)