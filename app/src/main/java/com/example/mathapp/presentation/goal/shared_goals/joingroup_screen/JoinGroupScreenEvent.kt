package com.example.mathapp.presentation.goal.shared_goals.joingroup_screen

sealed interface JoinGroupScreenEvent {
    data object Idle : JoinGroupScreenEvent
    data class OnGroupIdChange(val groupId: String) : JoinGroupScreenEvent
    data class AlertDialogStateChange(val isAlertBoxOpen: Boolean) : JoinGroupScreenEvent
    data object OnConfirmClick : JoinGroupScreenEvent
    data object OnSuccess : JoinGroupScreenEvent
}