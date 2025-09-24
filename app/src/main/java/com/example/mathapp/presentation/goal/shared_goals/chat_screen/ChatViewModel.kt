package com.example.mathapp.presentation.goal.shared_goals.chat_screen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.SharedGoalRepository
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])
    private val groupName: String = checkNotNull(savedStateHandle["groupName"])
    private val _state = MutableStateFlow(ChatScreenState(groupName = groupName, groupId = groupId))
    val state = _state.asStateFlow()

    init {
        getMessages()
        getCurrentUserId()
    }

    fun onEvent(event: ChatScreenEvent) {
        when (event) {
            is ChatScreenEvent.OnValueChange -> {
                _state.update {
                    it.copy(textMessage = event.text)
                }
            }

            ChatScreenEvent.SendMessage -> sendMessage()
        }
    }

    private fun sendMessage() {
        viewModelScope.launch(Dispatchers.IO) {

            val result = sharedGoalRepository.sendMessage(
                content = _state.value.textMessage.trim(),
                groupId = groupId
            )
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _state.update {
                        it.copy(textMessage = "")
                    }

                }.onFailure { error ->
                    this@launch.launch(Dispatchers.Main.immediate)
                    {
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = "Sending message failed: ${error.message}",
                                duration = SnackbarDuration.Short
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _state.update {
                it.copy(isLoading = true, error = null)
            }
            sharedGoalRepository.getMessages(groupId)
                .flowOn(Dispatchers.IO)
                .collect { supabaseOperation ->
                supabaseOperation.onSuccess { messages ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            messages = messages
                        )
                    }
                }.onFailure { exception ->
                    _state.update {
                        it.copy(isLoading = false, error = exception.message)
                    }
                }
            }
        }
    }

    private fun getCurrentUserId() {
        viewModelScope.launch {
            val userId = sharedGoalRepository.getCurrentUserId()
            _state.update {
                it.copy(currentUserId = userId)
            }
        }
    }
}

























