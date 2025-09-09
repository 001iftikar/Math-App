package com.example.mathapp.presentation.goal.shared_goals.groups_screen

import android.util.Log
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
class GroupViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository
) : ViewModel() {
    private val _groupsState = MutableStateFlow(GroupsScreenState())
    val groupsState = _groupsState.asStateFlow()

    init {
        getGroups()
    }

    fun onEvent(event: GroupsScreenEvent) {
        when (event) {
            GroupsScreenEvent.Refresh -> {
                getGroups()
            }
        }
    }

    private fun getGroups() {
        viewModelScope.launch {
            _groupsState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            sharedGoalRepository.getGroups().collect { supabaseOperation ->
                supabaseOperation.onSuccess { groups ->
                    _groupsState.update {
                        it.copy(
                            isLoading = false,
                            groups = groups
                        )
                    }
                }.onFailure { exception ->
                    _groupsState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
            }
        }
    }

    private fun getSpecificGroup(groupId: String) {
        viewModelScope.launch {
            sharedGoalRepository.getSpecificGroup(groupId).collect { supabaseOperation ->
                supabaseOperation.onSuccess { group ->
                    Log.d("Group", "getSpecificGroup: ${group.id} : ${group.name}")
                }
                    .onFailure {
                        Log.e("Group", "getSpecificGroup: $it", )
                    }
            }
        }
    }
}























