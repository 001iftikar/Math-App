package com.example.mathapp.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.SupabaseRepository
import com.example.mathapp.ui.goal.redirecting_screen.RedirectingScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupabaseSessionViewModel @Inject constructor(
    private val supabaseRepository: SupabaseRepository
) : ViewModel() {
    private val _userSessionState = MutableStateFlow(RedirectingScreenState())
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
                    // todo handle error
                }
            }
        }
    }
}