package com.example.mathapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAlertDialogComponent(
    text: String = "Are you sure?",
    state: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (state) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onDismissRequest
                        ) {
                            Text("No")
                        }
                        TextButton(
                            onClick = onConfirmClick
                        ) {
                            Text("Yes")
                        }
                    }
                }
            }
        )
    }
}