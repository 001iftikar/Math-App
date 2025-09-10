package com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen

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
class SpecificGroupViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val groupId: String = checkNotNull(savedStateHandle["groupId"])

    private val _specificGoalState = MutableStateFlow(SpecificGroupDetailsScreenState())
    val specificGoalState = _specificGoalState.asStateFlow()

    init {
        getGroup()
        getProfiles()
    }

    private fun getGroup() {
        viewModelScope.launch {
            sharedGoalRepository.getSpecificGroup(groupId).collect { supabaseOperation ->
                supabaseOperation.onSuccess { group ->
                    _specificGoalState.update {
                        it.copy(
                            group = group,
                            error = null
                        )
                    }
                }
                    .onFailure { exception ->
                        _specificGoalState.update {
                            it.copy(
                                error = exception.message
                            )
                        }
                    }
            }
        }
    }

    private fun getProfiles() {
        viewModelScope.launch {
            sharedGoalRepository.getGroupMembersForSpecificGroup(groupId).collect {

            }
        }
    }

}


























