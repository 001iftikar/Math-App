package com.iftikar.mathapp.presentation.goal.specific_goal_screen

sealed interface SpecificGoalEvent {
    object Idle : SpecificGoalEvent
    data class GetSpecificGoal(val goalId: String) : SpecificGoalEvent
    object NavigateBack : SpecificGoalEvent

    data class MarkAsCompleteStateChange(val state: Boolean) : SpecificGoalEvent
    data class OnCompletedConfirmClick(val goalId: String) : SpecificGoalEvent
}