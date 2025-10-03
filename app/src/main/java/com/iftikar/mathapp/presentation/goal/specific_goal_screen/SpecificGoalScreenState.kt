package com.iftikar.mathapp.presentation.goal.specific_goal_screen

import com.iftikar.mathapp.domain.model.GoalModel

data class SpecificGoalScreenState(
    val isLoading: Boolean = false,
    val goalModel: GoalModel? = null,
    val markAsCompletedMessage: String = "",
    val alertBoxState: Boolean = false,
    val error: String? = null
)
