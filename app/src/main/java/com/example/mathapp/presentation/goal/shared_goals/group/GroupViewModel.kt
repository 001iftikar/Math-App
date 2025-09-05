package com.example.mathapp.presentation.goal.shared_goals.group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.remote.model.GroupDto
import com.example.mathapp.domain.repository.SharedGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val sharedGoalRepository: SharedGoalRepository
) : ViewModel() {
    private val _groupsState = MutableStateFlow(GroupsScreenState())
    val groupsState = _groupsState.asStateFlow()

    init {
       // createGroup()
        getGroups()
    }
    private fun createGroup() {
        viewModelScope.launch {
            sharedGoalRepository.createGroup(
                GroupDto(
                    name = "Test",
                    description = "testing"
                )
            ).collect { supabaseOperation ->
                supabaseOperation.onSuccess { message ->
                    Log.d("Shared-Goal-View", "createGroup: $message")
                }.onFailure { exception ->
                    Log.e("Shared-Goal-View", "createGroup: $exception", )
                }
            }
        }
    }

    private fun getGroups() {
        viewModelScope.launch {
            sharedGoalRepository.getGroups().collect { supabaseOperation ->
                supabaseOperation.onSuccess {
                    Log.d("Shared-Goal-View", "getGroup: $it --> ${it.size}")
                }.onFailure { exception ->
                    Log.e("Shared-Goal-View", "getGroup: $exception")
                }
            }
        }
    }
}

























