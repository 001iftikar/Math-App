package com.example.mathapp.ui.goal.dashboard_screen

sealed interface DashboardEvent {
    data object Idle : DashboardEvent
    data object Refresh : DashboardEvent
    data class NavigateToSpecificGoal(val goalId: String) : DashboardEvent
    data object NavigateBack : DashboardEvent
    data class SortByEvent(val sortBy: SortBy) : DashboardEvent
}