package com.example.mathapp.domain.model

data class SharedGoal(
    val id: String,
    val groupId: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val createdAt: String,
    val endBy: String
)
