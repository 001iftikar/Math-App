package com.example.mathapp.presentation.goal.shared_goals.chat_screen

import android.util.Log
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
class ChatViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])
    private val groupName: String = checkNotNull(savedStateHandle["groupName"])
    private val _state = MutableStateFlow(ChatScreenState(groupName = groupName, groupId = groupId))
    val state =_state.asStateFlow()

    init {
        getMessages()
        getCurrentUserId()
    }

    fun onEvent(event: ChatScreenEvent) {
        when(event) {
            is ChatScreenEvent.OnValueChange -> {
                _state.update {
                    it.copy(textMessage = event.text)
                }
            }

            ChatScreenEvent.SendMessage -> sendTestMessage()
        }
    }

    private fun sendTestMessage() {
        viewModelScope.launch {
            val result = sharedGoalRepository.sendMessage(
                content = _state.value.textMessage.trim(),
                groupId = groupId
            )
            result.onSuccess {
                _state.update {
                    it.copy(textMessage = "")
                }
                Log.d("Messages", "sendTestMessage: Sent with $groupId")
            }.onFailure { error ->
                Log.e("Messages", "Failed to send test message: ${error.message}")
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, error = null)
            }
            sharedGoalRepository.getMessages(groupId).collect { supabaseOperation ->
                supabaseOperation.onSuccess {messages ->
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

























