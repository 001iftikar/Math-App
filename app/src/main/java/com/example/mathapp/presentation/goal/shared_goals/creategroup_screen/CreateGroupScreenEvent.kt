package com.example.mathapp.presentation.goal.shared_goals.creategroup_screen

sealed interface CreateGroupScreenEvent {
    data object Idle : CreateGroupScreenEvent
    data class OnTitleChange(val title: String) : CreateGroupScreenEvent
    data class OnDescriptionChange(val description: String) : CreateGroupScreenEvent
    data object OnCreateClick : CreateGroupScreenEvent
    data object OnSuccess : CreateGroupScreenEvent
}