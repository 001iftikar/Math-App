package com.example.mathapp.presentation.goal.finished_goals_screen

sealed interface FinishedGoalsScreenEvent {
    object Retry : FinishedGoalsScreenEvent
    data object OnDeleteButtonClick : FinishedGoalsScreenEvent
    data object OnDismissClick : FinishedGoalsScreenEvent
    data class OnDeleteConfirmClick(val goalId: String) : FinishedGoalsScreenEvent
}