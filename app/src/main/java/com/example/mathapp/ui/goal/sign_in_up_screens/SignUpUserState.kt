package com.example.mathapp.ui.goal.sign_in_up_screens

data class SignUpUserState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isPasswordVisible: Boolean = false,
    var passwordError: String? = null,
    val passwordEmptyError: String? = null,
    val emailEmptyError: String? = null,
    val nameEmptyError: String? = null,
    val buttonText: String = "Sign up",
    val error: String? = null,
    val isLoading: Boolean = false
)