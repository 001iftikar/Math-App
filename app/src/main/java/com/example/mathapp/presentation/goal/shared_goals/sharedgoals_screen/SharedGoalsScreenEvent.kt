package com.example.mathapp.presentation.goal.shared_goals.sharedgoals_screen

sealed interface SharedGoalsScreenEvent {
    data object Refresh : SharedGoalsScreenEvent
}
