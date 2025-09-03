package com.example.mathapp.presentation.snackbar

import androidx.compose.material3.SnackbarDuration

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null,
    val duration: SnackbarDuration
)
