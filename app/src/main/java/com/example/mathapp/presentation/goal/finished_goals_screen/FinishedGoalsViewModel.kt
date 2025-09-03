package com.example.mathapp.presentation.goal.finished_goals_screen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.UserGoalRepository
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
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
            is FinishedGoalsScreenEvent.OnDeleteButtonClick -> {
                _goalsState.update {
                    it.copy(
                        alertDialogState = true
                    )
                }
            }

            FinishedGoalsScreenEvent.OnDismissClick -> {
                _goalsState.update {
                    it.copy(
                        alertDialogState = false
                    )
                }
            }

            is FinishedGoalsScreenEvent.OnDeleteConfirmClick -> {
                deleteGoal(event.goalId)
                _goalsState.update {
                    it.copy(alertDialogState = false)
                }
                getUnfinishedGoals()
            }
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

    private fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            goalRepository.deleteFinishedGoal(goalId).collect { supabaseOperation ->
                supabaseOperation.onSuccess {
                    this.launch(Dispatchers.IO) {
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(
                                message = it,
                                duration = SnackbarDuration.Short
                            )
                        )
                    }
                }
            }
        }
    }
}





















