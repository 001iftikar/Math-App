package com.example.mathapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String? = null,
    val name: String = "",
    val email: String = ""
)