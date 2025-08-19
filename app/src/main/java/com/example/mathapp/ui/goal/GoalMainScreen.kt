package com.example.mathapp.ui.goal

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

@Composable
fun GoalMainScreen() {
    Scaffold { innperPadding ->
        Column(
            modifier = Modifier.padding(innperPadding)
        ) {
            Text(
                "Log in sign up will be here",
            )
        }

    }
}

sealed interface TestResult {
    data object Success : TestResult
    data class Error(val message: String?) : TestResult
}

class TestAuthManager(
    private val context: Context
) {
    private val supabase = createSupabaseClient(
        supabaseUrl = "https://ieuvzjskuopzrpfbiuae.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlldXZ6anNrdW9wenJwZmJpdWFlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTI4MjEwODYsImV4cCI6MjA2ODM5NzA4Nn0.Ir2L0E8lT5PDjRvM-QPDJXn8jVjN8VuRw7JPmYL0ITc",
    ) {
        install(Auth)
    }
}

























































