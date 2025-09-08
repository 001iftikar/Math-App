package com.example.mathapp.presentation.goal.shared_goals.joingroup_screen

data class JoinGroupScreenState(
    val groupId: String = "",
    val isAlertBoxOpen: Boolean = false,
    val error: String? = null
)
