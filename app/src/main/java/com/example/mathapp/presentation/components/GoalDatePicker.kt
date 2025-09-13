package com.example.mathapp.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun DatePickerComponent(
    title: String,
    datePickerState: DatePickerState,
    isOpenState: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (isOpenState) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel
                ) {
                    Text("Cancel")
                }
            },
            colors = DatePickerDefaults.colors(
                Color(0xFF0D0B1E)
            )
        ) {

            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxSize(),
                title = {
                    Text(
                        text = title,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFFF4081),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                colors = DatePickerDefaults.colors(
                    Color(0xFF0D0B1E)
                )
            )

        }
    }
}
