package com.example.mathapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String = "",
    val name: String = "",
    val email: String = ""
)