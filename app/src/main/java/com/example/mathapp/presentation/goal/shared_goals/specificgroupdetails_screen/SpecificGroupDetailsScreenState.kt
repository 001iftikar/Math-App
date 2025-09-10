package com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen

import com.example.mathapp.domain.model.Group
import com.example.mathapp.domain.model.UserProfile

data class SpecificGroupDetailsScreenState(
    val group: Group? = null,
    val belongedMembers: List<UserProfile> = emptyList(),
    val error: String? = null
)