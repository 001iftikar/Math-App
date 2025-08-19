package com.example.mathapp.ui.goal

data class SignUpUserState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isPasswordVisible: Boolean = false,
    var passwordError: String? = null,
    val error: String? = null
)