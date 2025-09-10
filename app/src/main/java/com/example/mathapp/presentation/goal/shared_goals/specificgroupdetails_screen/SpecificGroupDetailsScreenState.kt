package com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen

import com.example.mathapp.domain.model.Group

data class SpecificGroupDetailsScreenState(
    val group: Group? = null,
    val error: String? = null
)