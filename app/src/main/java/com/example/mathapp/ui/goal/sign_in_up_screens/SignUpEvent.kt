package com.example.mathapp.ui.goal.sign_in_up_screens

sealed class SignUpEvent {
    data object Idle: SignUpEvent()
    data class EnterEmail(val email: String) : SignUpEvent()
    data class EnterPassword(val password: String) : SignUpEvent()
    data class EnterName(val name: String) : SignUpEvent()
    data class Success(val userId: String) : SignUpEvent()
    object PasswordVisibilityChange : SignUpEvent()
    data class PasswordError(val passwordError: String) : SignUpEvent()

    data class PasswordEmptyError(val passwordEmptyError: String) : SignUpEvent()
    data class EmailEmptyError(val emailEmptyError: String) : SignUpEvent()
    data class NameEmptyError(val nameEmptyError: String) : SignUpEvent()
}