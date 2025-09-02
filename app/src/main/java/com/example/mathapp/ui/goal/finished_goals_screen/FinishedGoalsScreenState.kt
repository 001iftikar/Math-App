package com.example.mathapp.ui.goal.finished_goals_screen

import com.example.mathapp.domain.model.GoalModel

data class FinishedGoalsScreenState(
    val isLoading: Boolean = false,
    val goals: List<GoalModel>? = null,
    val error: String? = null
)
