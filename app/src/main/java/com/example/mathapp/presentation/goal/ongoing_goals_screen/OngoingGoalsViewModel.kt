package com.example.mathapp.presentation.goal.ongoing_goals_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.UserGoalRepository
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
class OngoingGoalsViewModel @Inject constructor(
    private val userGoalRepository: UserGoalRepository
) : ViewModel() {

    private val _goalsState = MutableStateFlow(DashboardScreenState())
    val goalState = _goalsState.asStateFlow()

    private val _ongoingGoalsScreenEvent: Channel<OngoingGoalsScreenEvent> = Channel()
    val dashboardEvent = _ongoingGoalsScreenEvent.receiveAsFlow()

    init {
        getOngoingGoals()
    }

    fun onEvent(event: OngoingGoalsScreenEvent) {
        when(event) {
            OngoingGoalsScreenEvent.Refresh -> getOngoingGoals()
            is OngoingGoalsScreenEvent.NavigateToSpecificGoal -> navigateToSpecificGoal(event.goalId)
            OngoingGoalsScreenEvent.NavigateBack -> navigateBack()

            is OngoingGoalsScreenEvent.SortByEvent -> {
                _goalsState.update {
                    it.copy(
                        sortBy = event.sortBy
                    )
                }
            }

            else -> Unit
        }
    }
    private fun getOngoingGoals() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _goalsState.update {
                it.copy(isLoading = true, error = null)
            }
            userGoalRepository.getAllGoals()
                .flowOn(Dispatchers.IO)
                .collect { supabaseOperation ->
                supabaseOperation.onSuccess {goals ->
                    _goalsState.update {state ->
                        state.copy(
                            isLoading = false,
                            goals = goals.filter { !it.isCompleted },
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

    private fun navigateToSpecificGoal(goalId: String) {
        viewModelScope.launch {
            _ongoingGoalsScreenEvent.send(OngoingGoalsScreenEvent.NavigateToSpecificGoal(goalId))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _ongoingGoalsScreenEvent.send(OngoingGoalsScreenEvent.NavigateBack)
        }
    }
}