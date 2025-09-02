package com.example.mathapp.presentation.goal.sign_in_up_screens

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mathapp.presentation.components.LoginBackgroundComponent
import com.example.mathapp.presentation.components.TextFieldComponent
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.ui.theme.Orange
import com.example.mathapp.ui.theme.PurpleGrey40


@Composable
fun GoalSignInScreen(
    supabaseUserCreateViewModel: SupabaseUserCreateViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state by supabaseUserCreateViewModel.createUserState.collectAsState()
    val eventState by supabaseUserCreateViewModel.eventState.collectAsState(initial = SignUpEvent.Idle)
    val onEvent = supabaseUserCreateViewModel::onEvent


    LaunchedEffect(eventState) {
        when (eventState) {
            is SignUpEvent.Success -> {
                navHostController.navigate(Routes.GoalHomeScreen) {
                    popUpTo<Routes.GoalSignUpScreen> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
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

                    onEvent(SignUpEvent.PasswordError("Hint: your password is of length six"))

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



            Button(
                onClick = {
                    if (state.email.isEmpty() || state.password.isEmpty()) {
                        if (state.email.isEmpty()) {
                            onEvent(SignUpEvent.EmailEmptyError("Please provide an email"))
                        }

                        if (state.password.isEmpty()) {
                            onEvent(SignUpEvent.PasswordEmptyError("Please provide a password"))
                        }
                        return@Button
                    }

                    supabaseUserCreateViewModel.signIn()
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
                        "Signing in..."
                    } else if (state.error != null) {
                        "Try again!"
                    } else {
                       "Log in"
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("New here?", color = Color.Green.copy(alpha = 0.8f))
                Spacer(Modifier.width(4.dp))

                Text("Create an account", color = Color.Green,
                    modifier = Modifier.clickable(
                        onClick = {
                            navHostController.navigate(Routes.GoalSignUpScreen) {
                                popUpTo<Routes.GoalSignUpScreen> {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
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