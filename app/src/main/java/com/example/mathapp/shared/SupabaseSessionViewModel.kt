package com.example.mathapp.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.SupabaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupabaseSessionViewModel @Inject constructor(
    private val supabaseRepository: SupabaseAuthRepository
) : ViewModel() {
    private val _userSessionState = MutableStateFlow(SupabaseSessionState())
    val userSessionState = _userSessionState.asStateFlow()

    init {
        loadUserSession()
    }

    fun loadUserSession() {
        viewModelScope.launch {
            supabaseRepository.loadUserSession().collect { supabaseOperation ->
                supabaseOperation.onSuccess { session ->
                    _userSessionState.update {
                        it.copy(
                            userSession = session
                        )
                    }
                }.onFailure { exception ->
                    _userSessionState.update {
                        it.copy(
                            error = exception.message
                        )
                    }
                }
            }
        }
    }
}

data class SupabaseSessionState(
    val userSession: UserSession? = null,
    val error: String? = null
)