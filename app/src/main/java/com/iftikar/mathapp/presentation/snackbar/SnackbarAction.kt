package com.iftikar.mathapp.presentation.snackbar

data class SnackbarAction(
    val name: String,
    val route: Any? = null // for navigation
)
