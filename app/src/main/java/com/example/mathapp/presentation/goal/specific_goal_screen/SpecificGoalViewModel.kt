package com.example.mathapp.presentation.goal.specific_goal_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.remote.model.GoalRequestDto
import com.example.mathapp.domain.repository.UserGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecificGoalViewModel @Inject constructor(
    private val userGoalRepository: UserGoalRepository
) : ViewModel() {

    private val _specificGoalState = MutableStateFlow(SpecificGoalScreenState())
    val specificGoalState = _specificGoalState.asStateFlow()

    private val _specificGoalEvent: Channel<SpecificGoalEvent> = Channel()
    val specificGoalEvent = _specificGoalEvent.receiveAsFlow()

    fun onEvent(event: SpecificGoalEvent) {
        when (event) {
            is SpecificGoalEvent.GetSpecificGoal -> {
                getSpecificGoal(event.goalId)
            }

            SpecificGoalEvent.NavigateBack -> navigateBack()

            is SpecificGoalEvent.MarkAsCompleteStateChange -> {
                _specificGoalState.update {
                    it.copy(
                        alertBoxState = event.state
                    )
                }
            }
            is SpecificGoalEvent.OnCompletedConfirmClick -> {
                markAsCompleted(goalId = event.goalId)
                _specificGoalState.update {
                    it.copy(alertBoxState = false)
                }
            }

            else -> Unit
        }
    }

    private fun getSpecificGoal(goalId: String) {
        _specificGoalState.update {
            it.copy(isLoading = true, error = null)
        }

        viewModelScope.launch {
            userGoalRepository.getSpecificGoal(goalId).collect { supabaseOperation ->
                supabaseOperation.onSuccess { goalModel ->
                    _specificGoalState.update {
                        it.copy(
                            isLoading = false,
                            goalModel = goalModel,
                            error = null
                        )
                    }
                    Log.d("Goal-View", "getSpecificGoal: $goalModel")
                }.onFailure { exception ->
                    _specificGoalState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
            }
        }
    }

    private fun markAsCompleted(goalId: String) {
        viewModelScope.launch {
            userGoalRepository.upsertGoal(
                GoalRequestDto(
                    id = goalId,
                    isCompleted = true
                )
            ).collect { supabaseOperation ->
                supabaseOperation
                    .onSuccess { text ->
                        _specificGoalState.update {
                            it.copy(markAsCompletedMessage = "Mark as completed")
                        }
                    }
                    .onFailure { exception ->
                        _specificGoalState.update {
                            it.copy(error = "Operation not completed!")
                        }
                    }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _specificGoalEvent.send(SpecificGoalEvent.NavigateBack)
        }
    }
}

























