package com.example.mathapp.ui.goal.finished_goals_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.UserGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedGoalsViewModel @Inject constructor(
    private val goalRepository: UserGoalRepository
) : ViewModel() {
    private val _goalsState = MutableStateFlow(FinishedGoalsScreenState())
    val goalsState = _goalsState.asStateFlow()

    init {
        getUnfinishedGoals()
    }

    fun onEvent(event: FinishedGoalsScreenEvent) {
        when(event) {
            FinishedGoalsScreenEvent.Retry -> getUnfinishedGoals()
        }
    }

    private fun getUnfinishedGoals() {
        viewModelScope.launch(Dispatchers.IO) {
            _goalsState.update {
                it.copy(isLoading = true, error = null)
            }

            goalRepository.getAllGoals().collect { supabaseOperation ->
                supabaseOperation
                    .onSuccess { goals ->
                    _goalsState.update { state ->
                        state.copy(
                            isLoading = false,
                            goals = goals.filter { it.isCompleted }
                        )
                    }
                }
                    .onFailure { exception ->
                        _goalsState.update {
                            it.copy(isLoading = false, error = exception.message)
                        }
                    }
            }
        }
    }
}





















