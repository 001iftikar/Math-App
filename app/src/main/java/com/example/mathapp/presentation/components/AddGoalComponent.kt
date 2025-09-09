package com.example.mathapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AddGoalComponent(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    date: String,
    saveEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPickDateClick: () -> Unit,
    onAddGoalClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        TextFieldComponent(
            value = title,
            onValueChange = { onTitleChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            label = "Title",
            labelColor = Color.White.copy(0.6f),
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.Black
        )

        TextFieldComponent(
            value = description,
            onValueChange = { onDescriptionChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            label = "Description",
            labelColor = Color.White.copy(0.6f),
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.Black
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Magenta,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .width(200.dp)
                    .padding(vertical = 5.dp, horizontal = 10.dp)
            )

            Button(
                onClick = onPickDateClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Magenta,
                    contentColor = Color.Black
                )
            ) {
                Text("Complete by?", fontSize = 16.sp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onAddGoalClick,
                enabled = saveEnabled,
                modifier = Modifier
                    .width(120.dp)
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFFF4081)),
                        shape = RoundedCornerShape(12.dp)
                    )

            ) {
                Text("A  I  M", color = Color(0xFFFFD54F))
            }

            TextButton(
                onClick = onCancelClick,
                modifier = Modifier
                    .width(120.dp)
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFFFD54F)),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text("C  A  N  C  E  L", color = Color(0xFFFF4081))
            }
        }
    }
}
