package com.iftikar.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen

import com.iftikar.mathapp.domain.model.Group
import com.iftikar.mathapp.domain.model.UserProfile

data class SpecificGroupDetailsScreenState(
    val group: Group? = null,
    val isAdmin: Boolean = false,
    val belongedMembers: List<UserProfile> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean
)