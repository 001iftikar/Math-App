package com.example.mathapp.presentation.goal.shared_goals.groups_screen

sealed interface GroupsScreenEvent {
    data object Refresh : GroupsScreenEvent
}