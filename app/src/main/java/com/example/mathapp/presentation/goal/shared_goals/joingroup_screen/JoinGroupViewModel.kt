package com.example.mathapp.presentation.goal.shared_goals.joingroup_screen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.SharedGoalRepository
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
class JoinGroupViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository
) : ViewModel() {
    private val _joinGroupScreenState = MutableStateFlow(JoinGroupScreenState())
    val joinGroupScreenState = _joinGroupScreenState.asStateFlow()

    private val _joinGroupEvent: Channel<JoinGroupScreenEvent> = Channel()
    val joinGroupEvent = _joinGroupEvent.receiveAsFlow()


    fun onEvent(event: JoinGroupScreenEvent) {
        when(event) {
            is JoinGroupScreenEvent.AlertDialogStateChange -> {
                _joinGroupScreenState.update {
                    it.copy(isAlertBoxOpen = event.isAlertBoxOpen, error = null)
                }
            }

            JoinGroupScreenEvent.OnConfirmClick -> {
                _joinGroupScreenState.update {
                    it.copy(isAlertBoxOpen = false)
                }

                joinGroup()
            }

            is JoinGroupScreenEvent.OnGroupIdChange -> {
                _joinGroupScreenState.update {
                    it.copy(groupId = event.groupId)
                }
            }

            else -> Unit
        }
    }

    private fun joinGroup() {
        viewModelScope.launch {
            sharedGoalRepository.joinGroup(_joinGroupScreenState.value.groupId)
                .flowOn(Dispatchers.IO)
                .collect { supabaseOperation ->
                supabaseOperation.onSuccess { message ->
                    this.launch(Dispatchers.Main) {
                        _joinGroupEvent.send(JoinGroupScreenEvent.OnSuccess)
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        )
                    }
                }
                    .onFailure {exception ->
                        _joinGroupScreenState.update {
                            it.copy(
                                error = exception.message
                            )
                        }
                    }
            }
        }
    }
}
