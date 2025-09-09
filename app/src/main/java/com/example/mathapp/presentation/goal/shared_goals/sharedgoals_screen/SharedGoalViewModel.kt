package com.example.mathapp.presentation.goal.shared_goals.sharedgoals_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.SharedGoalRepository
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
    private val _sharedGoals = MutableStateFlow(SharedGoalsScreenState(groupId = groupId))
    val sharedGoals = _sharedGoals.asStateFlow()

    init {
        getSharedGoals()
    }

    fun onEvent(event: SharedGoalsScreenEvent) {
        when(event) {
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
}





















