package com.iftikar.mathapp.presentation.goal.finished_goals_screen

import com.iftikar.mathapp.domain.model.GoalModel

data class FinishedGoalsScreenState(
    val isLoading: Boolean = false,
    val goals: List<GoalModel>? = null,
    val alertDialogState: Boolean = false,
    val error: String? = null
)
