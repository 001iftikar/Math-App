package com.example.mathapp.presentation.goal.finished_goals_screen

sealed interface FinishedGoalsScreenEvent {
    object Retry : FinishedGoalsScreenEvent
}