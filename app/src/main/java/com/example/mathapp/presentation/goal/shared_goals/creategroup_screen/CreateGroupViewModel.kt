package com.example.mathapp.presentation.goal.shared_goals.creategroup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.remote.model.GroupDto
import com.example.mathapp.domain.repository.SharedGoalRepository
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
class CreateGroupViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository
) : ViewModel() {
    private val _createGoalScreenState = MutableStateFlow(CreateGroupScreenState())
    val createGoalScreenState = _createGoalScreenState.asStateFlow()

    private val _createGroupEventState: Channel<CreateGroupScreenEvent> = Channel()
    val createGroupEventState = _createGroupEventState.receiveAsFlow()
    fun onEvent(event: CreateGroupScreenEvent) {
        when(event) {
            is CreateGroupScreenEvent.OnTitleChange -> {
                _createGoalScreenState.update {
                    it.copy(title = event.title)
                }
            }

            is CreateGroupScreenEvent.OnDescriptionChange -> {
                _createGoalScreenState.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            CreateGroupScreenEvent.OnCreateClick -> createGroup()
            else -> Unit
        }
    }

    private fun createGroup() {
        viewModelScope.launch {
            _createGoalScreenState.update {
                it.copy(isLoading = true, error = null)
            }
            sharedGoalRepository.createGroup(
                GroupDto(
                    name = _createGoalScreenState.value.title,
                    description = _createGoalScreenState.value.description
                )
            ).collect { supabaseOperation ->
                supabaseOperation.onSuccess {message ->
                    _createGoalScreenState.update {
                        it.copy(isLoading = false)
                    }
                    this.launch(Dispatchers.IO) {
                        _createGroupEventState.send(CreateGroupScreenEvent.OnSuccess)
                    }
                }
                    .onFailure { exception ->
                        _createGoalScreenState.update {
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
























