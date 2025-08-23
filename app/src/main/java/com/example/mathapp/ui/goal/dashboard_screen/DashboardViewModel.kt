package com.example.mathapp.ui.goal.dashboard_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.UserGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userGoalRepository: UserGoalRepository
) : ViewModel() {

    private val _goalsState = MutableStateFlow(DashboardScreenState())
    val goalState = _goalsState.asStateFlow()

    init {
        getAllGoals()
    }

    fun onEvent(event: DashboardEvent) {
        when(event) {
            DashboardEvent.Refresh -> getAllGoals()
        }
    }
    private fun getAllGoals() {
        _goalsState.update {
            it.copy(isLoading = true, error = null)
        }
        viewModelScope.launch {
            userGoalRepository.getAllGoals().collect { supabaseOperation ->
                supabaseOperation.onSuccess {goals ->
                    _goalsState.update {
                        it.copy(
                            isLoading = false,
                            goals = goals,
                            error = null
                        )
                    }
                }.onFailure { exception ->
                    _goalsState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
            }
        }
    }
}