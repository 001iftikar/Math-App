package com.example.mathapp.presentation.goal.insert_goal_screen

sealed interface AddGoalScreenEvent {
    data object Idle : AddGoalScreenEvent
    data class EnterTitle(val title: String) : AddGoalScreenEvent
    data class EnterDescription(val description: String) : AddGoalScreenEvent
    data object SaveGoal : AddGoalScreenEvent
    data object OnPickDateButtonClick : AddGoalScreenEvent
    data object OnDatePickerDismiss : AddGoalScreenEvent
    data object OnDatePickerDismissRequest : AddGoalScreenEvent
    data class OnSupabaseDateSelected(val date: String) : AddGoalScreenEvent
    data object OnAddedSuccess : AddGoalScreenEvent
    data object OnDialogDismissRequest : AddGoalScreenEvent

}