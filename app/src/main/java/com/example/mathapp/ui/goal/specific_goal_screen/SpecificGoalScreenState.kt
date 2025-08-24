package com.example.mathapp.ui.goal.specific_goal_screen

import com.example.mathapp.domain.model.GoalModel

data class SpecificGoalScreenState(
    val isLoading: Boolean = false,
    val goalModel: GoalModel? = null,
    val error: String? = null
)
