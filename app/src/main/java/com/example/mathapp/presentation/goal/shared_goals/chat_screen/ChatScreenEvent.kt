package com.example.mathapp.presentation.goal.shared_goals.chat_screen

sealed interface ChatScreenEvent {
    data class OnValueChange(val text: String) : ChatScreenEvent
    data object SendMessage : ChatScreenEvent
}