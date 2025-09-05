package com.example.mathapp.presentation.goal.shared_goals.group

import com.example.mathapp.domain.model.Group

data class GroupsScreenState(
    val groups: List<Group>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
