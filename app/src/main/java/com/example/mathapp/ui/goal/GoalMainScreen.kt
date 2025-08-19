package com.example.mathapp.ui.goal

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun GoalMainScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    Scaffold { innperPadding ->
        Column(
            modifier = Modifier.padding(innperPadding)
        ) {
            Text(
                "Log in sign up will be here",
            )

            TextField(
                value = email,
                onValueChange = {email = it}
            )

            TextField(
                value = password,
                onValueChange = {password = it}
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        TestAuthManager().signin(
                            emailValue = email,
                            passwordValue = password,
                        ).collect { result ->
                            when (result) {
                                is TestResult.Success -> resultMessage = "Sign up successful!"
                                is TestResult.Error -> resultMessage =
                                    result.message ?: "Unknown error"
                            }
                        }
                    }
                }
            ) {
                Text("Go")
            }

            resultMessage?.let {
                Text(it)
            }
        }

    }
}

sealed interface TestResult {
    data object Success : TestResult
    data class Error(val message: String?) : TestResult
}

class TestAuthManager(
) {
    private val supabase = createSupabaseClient(
        supabaseUrl = "https://ieuvzjskuopzrpfbiuae.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlldXZ6anNrdW9wenJwZmJpdWFlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTI4MjEwODYsImV4cCI6MjA2ODM5NzA4Nn0.Ir2L0E8lT5PDjRvM-QPDJXn8jVjN8VuRw7JPmYL0ITc",
    ) {
        install(Auth)
    }

    fun signUp(emailValue: String, passwordValue: String): Flow<TestResult> = flow {
        try {
            supabase.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
            }
            Log.d("Supabase", "signUp: Success")
            emit(TestResult.Success)
        } catch (e: Exception) {
            Log.e("Supabase", "signUp: ${e.localizedMessage}")
            emit(TestResult.Error(e.localizedMessage))
        }
    }

    fun signin(emailValue: String, passwordValue: String): Flow<TestResult> = flow {
        try {
            supabase.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            Log.d("Supabase", "signIn: Success")
            emit(TestResult.Success)
        } catch (e: Exception) {
            Log.e("Supabase", "signIn: ${e.localizedMessage}")
            emit(TestResult.Error(e.localizedMessage))
        }
    }

}


























































