package com.example.mathapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    value: String,
    label: String? = null,
    onValueChange: (String) -> Unit,
    labelColor: Color = Color.Unspecified,
    supportingText: @Composable (() -> Unit)? = null,
    focusedTextColor: Color = Color.Unspecified,
    unfocusedTextColor: Color = Color.Unspecified,
    unfocusedContainerColor: Color = Color.Unspecified,
    focusedContainerColor: Color = Color.Unspecified,
    focusedIndicatorColor: Color = Color.Unspecified,
    unfocusedIndicatorColor: Color = Color.Unspecified,
    cursorColor: Color = Color.Unspecified,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {



        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = {
                if (label != null) {
                    Text(label, color = labelColor)
                }
            },
            modifier = modifier.fillMaxWidth(),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            supportingText = supportingText,
            colors = TextFieldDefaults.colors(
                focusedTextColor = focusedTextColor,
                unfocusedTextColor = unfocusedTextColor,
                unfocusedContainerColor = unfocusedContainerColor,
                focusedContainerColor = focusedContainerColor,
                focusedIndicatorColor = focusedIndicatorColor,
                unfocusedIndicatorColor = unfocusedIndicatorColor,
                cursorColor = cursorColor
            )
        )
}
