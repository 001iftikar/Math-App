package com.example.mathapp.ui.goal.finished_goals_screen

sealed interface FinishedGoalsScreenEvent {
    object Retry : FinishedGoalsScreenEvent
}