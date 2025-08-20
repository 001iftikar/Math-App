package com.example.mathapp.ui.goal

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mathapp.ui.components.LoginBackgroundComponent
import com.example.mathapp.ui.components.TextFieldComponent
import com.example.mathapp.ui.theme.Orange
import com.example.mathapp.ui.theme.PurpleGrey40

@Composable
fun GoalSignUpScreen(
    supabaseUserCreateViewModel: SupabaseUserCreateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by supabaseUserCreateViewModel.createUserState.collectAsState()
    val eventState by supabaseUserCreateViewModel.eventState.collectAsState(initial = SignUpEvent.Idle)
    val onEvent = supabaseUserCreateViewModel::onEvent


    LaunchedEffect(eventState) {
        when (eventState) {
            is SignUpEvent.Success -> {
                Toast
                    .makeText(
                        context,
                        (eventState as SignUpEvent.Success).userId,
                        Toast.LENGTH_LONG
                    )
                    .show()
            }

            else -> {}
        }
    }

    Scaffold { innerPadding ->
        LoginBackgroundComponent(
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextFieldComponent(
                modifier = Modifier.padding(all = 8.dp),
                value = state.email,
                label = if (state.emailEmptyError != null) state.emailEmptyError else "Enter email",
                labelColor = if (state.emailEmptyError != null) Color.Red else PurpleGrey40,
                onValueChange = {
                    onEvent(SignUpEvent.EnterEmail(it))
                    if (it.isNotEmpty() && state.emailEmptyError != null) {
                        onEvent(SignUpEvent.EmailEmptyError(""))
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black.copy(alpha = 0.6f),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Orange
            )

            TextFieldComponent(
                modifier = Modifier.padding(horizontal = 8.dp),
                value = state.password,
                label = if (state.passwordEmptyError != null) state.passwordEmptyError else "Enter password",
                labelColor = if (state.passwordEmptyError != null) Color.Red else PurpleGrey40,
                onValueChange = {
                    onEvent(SignUpEvent.EnterPassword(it))

                    onEvent(SignUpEvent.PasswordError("Password must at least have 6 characters"))

                    if (it.isNotEmpty() && state.passwordEmptyError != null) {
                        onEvent(SignUpEvent.PasswordEmptyError(""))
                    }

                },
                supportingText = {
                    Text(
                        text = state.passwordError ?: "",
                        color = Color.Red

                    )
                },
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black.copy(alpha = 0.6f),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    val icon =
                        if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(
                        onClick = {
                            onEvent(SignUpEvent.PasswordVisibilityChange)
                        }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (state.isPasswordVisible) Color.Red else Color.Green
                        )
                    }

                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Orange
            )

            TextFieldComponent(
                modifier = Modifier.padding(horizontal = 8.dp),
                value = state.name,
                label = if (state.nameEmptyError != null) state.nameEmptyError else "Enter name",
                labelColor = if (state.nameEmptyError != null) Color.Red else PurpleGrey40,
                onValueChange = {
                    onEvent(SignUpEvent.EnterName(it))
                    if (it.isNotEmpty() && state.nameEmptyError != null) {
                        onEvent(SignUpEvent.NameEmptyError(""))
                    }
                },
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black.copy(alpha = 0.6f),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Orange
            )


            Button(
                onClick = {
                    if (state.email.isEmpty() || state.password.isEmpty() || state.name.isEmpty()) {
                        if (state.email.isEmpty()) {
                            onEvent(SignUpEvent.EmailEmptyError("Please provide an email"))
                        }

                        if (state.password.isEmpty()) {
                            onEvent(SignUpEvent.PasswordEmptyError("Please provide a password"))
                        }

                        if (state.name.isEmpty()) {
                            onEvent(SignUpEvent.NameEmptyError("I want to know your name!"))
                        }
                        return@Button
                    }

                    supabaseUserCreateViewModel.signUp()
                },
                enabled = state.passwordError == null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    disabledContainerColor = Color.Green.copy(alpha = 0.6f),
                    contentColor = if (state.error != null) Color.Red else Color.Unspecified
                )
            ) {
                Text(
                    text = if (state.isLoading) {
                        "Signing up"
                    } else if (state.error != null) {
                        "Try again!"
                    } else {
                        state.buttonText
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account?", color = Color.Green.copy(alpha = 0.8f))
                Spacer(Modifier.width(4.dp))

                Text("Log in", color = Color.Green,
                    modifier = Modifier.clickable(
                        onClick = {

                        }
                    ))

            }

            Text(
                text = state.error ?: "",
                color = Color.Red
            )

        }
    }
}




























































