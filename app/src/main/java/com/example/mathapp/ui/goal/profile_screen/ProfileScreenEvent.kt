package com.example.mathapp.ui.goal.profile_screen

sealed interface ProfileScreenEvent {
    data object Idle : ProfileScreenEvent
    data object LogoutClick : ProfileScreenEvent
    data object DismissRequest : ProfileScreenEvent
    data object ConfirmClick : ProfileScreenEvent
    data object OnLogout : ProfileScreenEvent
}