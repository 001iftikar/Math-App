package com.example.mathapp.ui.goal.dashboard_screen

import com.example.mathapp.domain.model.GoalModel
import io.github.jan.supabase.auth.user.UserSession

data class DashboardScreenState(
    val goals: List<GoalModel>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortBy: SortBy = SortBy.NAMEASC
)

enum class SortBy(val title: String){
    NAMEASC(title = "Sort by Name Asc"),
    NAMEDSC(title = "Sort by Name Dsc"),
    CREATEDAT(title = "Sort by creation"),
    ENDBY(title = "Sort by deadline")
}

