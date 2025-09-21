package com.example.mathapp.presentation.goal.shared_goals.sharedgoals_screen

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.SharedGoalRepository
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedGoalViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])
    private val groupName: String = checkNotNull(savedStateHandle["groupName"])
    private val _sharedGoals =
        MutableStateFlow(SharedGoalsScreenState(groupId = groupId, groupName = groupName))
    val sharedGoals = _sharedGoals.asStateFlow()

    init {
        getSharedGoals()
        isGroupAdmin()
    }

    fun onEvent(event: SharedGoalsScreenEvent) {
        when (event) {
            SharedGoalsScreenEvent.Refresh -> getSharedGoals()
        }
    }


    private fun getSharedGoals() {
        viewModelScope.launch {
            _sharedGoals.update {
                it.copy(
                    isLoading = true, error = null
                )
            }

            sharedGoalRepository.getSharedGoalsForGroup(groupId).collect { supabaseOperation ->
                supabaseOperation.onSuccess { sharedGoals ->
                    _sharedGoals.update {
                        it.copy(
                            isLoading = false,
                            goals = sharedGoals
                        )
                    }
                }
                    .onFailure { exception ->
                        _sharedGoals.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message
                            )
                        }
                    }
            }
        }
    }

    private fun isGroupAdmin() {
        viewModelScope.launch {
            sharedGoalRepository.isGroupAdmin(groupId).collect { supabaseOperation ->
                supabaseOperation.onSuccess { isAdmin ->
                    _sharedGoals.update {
                        it.copy(isAdmin = isAdmin)
                    }
                }
                    .onFailure { exception ->
                        Log.e("Admin-check", "isGroupAdmin: ${exception.message}")
                    }
            }
        }
    }

    fun markAsComplete(isCompleted: Boolean, sharedGoalId: String) {
        viewModelScope.launch {
            sharedGoalRepository.markAsCompleted(isCompleted, sharedGoalId).onSuccess {
                _sharedGoals.update { currentState ->
                    val updatedGoals = currentState.goals?.map { goal ->
                        if (goal.id == sharedGoalId) {
                            goal.copy(isCompleted = isCompleted)
                        } else {
                            goal
                        }
                    }
                    currentState.copy(goals = updatedGoals)
                }
                this.launch {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = if (isCompleted) "Goal is marked as completed" else "Goal is marked as ongoing",
                            duration = SnackbarDuration.Short
                        )
                    )
                }
            }.onFailure {
                this.launch {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Operation failed: ${it.message}",
                            duration = SnackbarDuration.Short
                        )
                    )
                }
            }
        }
    }
}





















