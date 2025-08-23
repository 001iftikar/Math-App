package com.example.mathapp.domain.model

data class GoalModel (
    val id: String,
    val createdAt: String,
    val title: String,
    val description: String,
    val endBy: String,
    val isCompleted: Boolean
)
