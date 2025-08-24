package com.example.mathapp.ui.goal.insert_goal_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.remote.model.GoalRequestDto
import com.example.mathapp.domain.repository.UserGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertGoalViewModel @Inject constructor(
    private val userGoalRepository: UserGoalRepository
) : ViewModel() {

    private val _insertGoalState = MutableStateFlow(AddGoalScreenState())
    val insertGoalState = _insertGoalState.asStateFlow()

    private val _eventState: Channel<AddGoalScreenEvent> = Channel()
    val eventState = _eventState.receiveAsFlow()

    fun onEvent(event: AddGoalScreenEvent) {
        when (event) {
            is AddGoalScreenEvent.EnterTitle -> {
                _insertGoalState.update {
                    it.copy(title = event.title)
                }
            }

            is AddGoalScreenEvent.EnterDescription -> {
                _insertGoalState.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            AddGoalScreenEvent.SaveGoal -> insertGoal()
            AddGoalScreenEvent.OnDatePickerDismiss -> {
                _insertGoalState.update {
                    it.copy(isDatePickerOpen = false)
                }
            }

            AddGoalScreenEvent.OnPickDateButtonClick -> {
                _insertGoalState.update {
                    it.copy(
                        isDatePickerOpen = true
                    )
                }
            }

            AddGoalScreenEvent.OnDatePickerDismissRequest -> {
                _insertGoalState.update {
                    it.copy(isDatePickerOpen = false)
                }
            }


            is AddGoalScreenEvent.OnSupabaseDateSelected -> {
                _insertGoalState.update {
                    it.copy(
                        supabaseSavableDate = event.date,
                        isDatePickerOpen = false
                    )
                }
            }

            AddGoalScreenEvent.OnDialogDismissRequest -> {
                _insertGoalState.update {
                    it.copy(
                        isDialogOpen = false
                    )
                }
            }
            else -> Unit
        }
    }


    private fun insertGoal() {
        viewModelScope.launch {
            userGoalRepository.insertGoal(
                GoalRequestDto(
                    title = _insertGoalState.value.title,
                    description = _insertGoalState.value.description,
                    endBy = _insertGoalState.value.supabaseSavableDate
                )
            ).collect { supabaseOperation ->
                supabaseOperation.onSuccess {
                    Log.d("Goal-View", "insertGoal: $it")
                    viewModelScope.launch(Dispatchers.IO) {
                        _insertGoalState.update {state ->
                            state.copy(isDialogOpen = true, goalAddMessage = it)
                        }
                        delay(1500L)
                        _insertGoalState.update { state ->
                            state.copy(
                                isDialogOpen = false, goalAddMessage = ""
                            )
                        }
                        _eventState.send(
                            AddGoalScreenEvent.OnAddedSuccess
                        )
                    }

                }.onFailure { exception ->
                    Log.e("Goal-View-Error", "insertGoal: ${exception.message}")
                }
            }
        }
    }
}