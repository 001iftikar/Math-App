package com.iftikar.mathapp.domain.model

data class Group(
    val id: String,
    val name: String,
    val admin: String,
    val description: String?,
    val createdAt: String
)
