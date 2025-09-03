package com.example.mathapp.presentation.snackbar

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null
)
