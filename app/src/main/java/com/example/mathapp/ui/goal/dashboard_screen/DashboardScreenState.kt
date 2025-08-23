package com.example.mathapp.ui.goal.dashboard_screen

import com.example.mathapp.domain.model.GoalModel
import io.github.jan.supabase.auth.user.UserSession

data class DashboardScreenState(
    val goals: List<GoalModel>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

