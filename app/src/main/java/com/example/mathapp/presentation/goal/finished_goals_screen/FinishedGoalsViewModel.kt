package com.example.mathapp.presentation.goal.finished_goals_screen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.UserGoalRepository
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedGoalsViewModel @Inject constructor(
    private val goalRepository: UserGoalRepository
) : ViewModel() {
    private val _goalsState = MutableStateFlow(FinishedGoalsScreenState())
    val goalsState = _goalsState.asStateFlow()
    private val _deleteEvent: Channel<Boolean> = Channel()
    val deleteEvent = _deleteEvent.receiveAsFlow()

    init {
        getFinishedGoals()
    }

    fun onEvent(event: FinishedGoalsScreenEvent) {
        when(event) {
            FinishedGoalsScreenEvent.Refresh -> {
                getFinishedGoals()
                viewModelScope.launch {
                    _deleteEvent.send(false)
                }
            }
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
            }
        }
    }

    private fun getFinishedGoals() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
                _goalsState.update {
                    it.copy(isLoading = true, error = null)
                }

            goalRepository.getAllGoals()
                .flowOn(Dispatchers.IO)
                .collect { supabaseOperation ->
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
        viewModelScope.launch(Dispatchers.IO) {
            goalRepository.deleteFinishedGoal(goalId).collect { supabaseOperation ->
                    supabaseOperation.onSuccess {
                        viewModelScope.launch(Dispatchers.Main) {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    message = it,
                                    duration = SnackbarDuration.Short
                                )
                            )

                            _deleteEvent.send(true)
                        }
                    }.onFailure { exception ->
                        viewModelScope.launch(Dispatchers.Main) {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    message = exception.message ?: "Unexpected error occurred",
                                    duration = SnackbarDuration.Long
                                )
                            )
                        }
                    }
            }
        }
    }
}





















