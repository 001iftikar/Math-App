package com.example.mathapp.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    isOpen: Boolean,
    title: String,
    bodyText: String,
    confirmButtonText: String = "Delete",
    onDismissRequest: () -> Unit,
    onConfirmButton: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(title)
            },
            text = {
                Text(bodyText)
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButton
                ) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}