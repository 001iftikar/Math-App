package com.example.mathapp.domain.model

import kotlinx.datetime.LocalDateTime

data class GoalModel (
    val id: String,
    val createdAt: LocalDateTime,
    val title: String,
    val description: String,
    val endBy: LocalDateTime,
    val isCompleted: Boolean
)
