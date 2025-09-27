package com.example.mathapp.presentation.goal.shared_goals.addsharedgoal_screen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.remote.model.SharedGoalDto
import com.example.mathapp.domain.repository.SharedGoalRepository
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSharedGoalViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository
) : ViewModel() {
    private val _addSharedGoalState = MutableStateFlow(AddSharedGoalScreenState())
    val addSharedGoalState = _addSharedGoalState.asStateFlow()

    private val _addGoalEventState: Channel<AddSharedGoalScreenEvent> = Channel()
    val addGoalEventState = _addGoalEventState.receiveAsFlow()

    fun onEvent(event: AddSharedGoalScreenEvent) {
        when (event) {
            is AddSharedGoalScreenEvent.OnTitleChange -> {
                _addSharedGoalState.update {
                    it.copy(title = event.title)
                }
            }

            is AddSharedGoalScreenEvent.OnDescriptionChange -> {
                _addSharedGoalState.update {
                    it.copy(description = event.description)
                }
            }

            is AddSharedGoalScreenEvent.DatePickerStateChange -> {
                _addSharedGoalState.update {
                    it.copy(
                        isDatePickerOpen = event.isOpen
                    )
                }
            }

            is AddSharedGoalScreenEvent.OnDateSelectConfirm -> {
                _addSharedGoalState.update {
                    it.copy(
                        date = event.date,
                        isDatePickerOpen = false
                    )
                }
            }

            is AddSharedGoalScreenEvent.OnCreateGoal -> createSharedGoal(event.groupId)

            else -> Unit
        }
    }

    private fun createSharedGoal(groupId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            sharedGoalRepository.createSharedGoal(
                SharedGoalDto(
                    group_id = groupId,
                    title = _addSharedGoalState.value.title,
                    description = _addSharedGoalState.value.description,
                    end_by = _addSharedGoalState.value.date
                )
            ).onSuccess { message ->
                viewModelScope.launch(Dispatchers.Main) {
                    _addGoalEventState.send(AddSharedGoalScreenEvent.OnSuccess)

                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = message,
                            duration = SnackbarDuration.Short
                        )
                    )
                }
            }
                .onFailure { exception ->
                    viewModelScope.launch(Dispatchers.Main) {
                        _addGoalEventState.send(AddSharedGoalScreenEvent.OnSuccess)

                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = exception.message ?: "Unexpected error occurred",
                                duration = SnackbarDuration.Short
                            )
                        )
                    }
                }
        }
    }
}

























