package com.example.mathapp.ui.goal.dashboard_screen

sealed interface DashboardEvent {
    data object Refresh : DashboardEvent
}