package com.example.mathapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mathapp.domain.model.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectDialog(
    isOpen: Boolean,
    title: String = "Add/Update Subject",
    selectedColors: List<Color>,
    subjectName: String,
    goalHours: String,
    onColorChange: (List<Color>) -> Unit,
    onSubjectNameChange: (String) -> Unit,
    onGoalHoursChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmButton: () -> Unit,
) {
    var subjectNameError by remember { mutableStateOf<String?>(null) }
    var goalHoursError by remember { mutableStateOf<String?>(null) }

    subjectNameError = when {
        subjectName.isBlank() -> "Please add a subject to study!"
        subjectName.length <= 2 -> "I can't think of a Subject which has less than 3 letters!"
        subjectName.length > 20 -> "Subject too long!!!"
        else -> null
    }

    goalHoursError = when {
        goalHours.isBlank() -> "Please enter goal study hours!"
        goalHours.toFloatOrNull() == null -> "Invalid Number!"
        goalHours.toFloat() < 1f -> "At least Study for 1 hour!"
        goalHours.toFloat() > 15f -> "Do not study a subject for too long!"
        else -> null
    }

    if (isOpen) {
        AlertDialog(onDismissRequest = onDismissRequest, title = { Text(title) }, text = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Subject.subjectCardColors.forEach { colors ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 1.dp, color = if (colors == selectedColors) {
                                        Color.Black
                                    } else Color.Transparent, shape = CircleShape
                                )
                                .background(brush = Brush.verticalGradient(colors))
                                .clickable { onColorChange(colors) }) {}
                    }
                }
                OutlinedTextField(
                    value = subjectName,
                    onValueChange = {
                        onSubjectNameChange(it)
                    },
                    label = { Text("Subject Name") },
                    singleLine = true,
                    isError = subjectNameError != null && subjectName.isNotBlank(),
                    supportingText = { Text(text = subjectNameError.orEmpty()) })
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = goalHours,
                    onValueChange = { onGoalHoursChange(it) },
                    label = { Text("Goal Study Hours") },
                    singleLine = true,
                    isError = goalHoursError != null && goalHours.isBlank(),
                    supportingText = { Text(text = goalHoursError.orEmpty()) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }, confirmButton = {
            TextButton(
                onClick = onConfirmButton,
                enabled = subjectNameError == null && goalHoursError == null
            ) {
                Text("Save")
            }
        }, dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        })
    }
}






































