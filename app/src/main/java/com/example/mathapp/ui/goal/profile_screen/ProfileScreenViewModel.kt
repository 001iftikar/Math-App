package com.example.mathapp.ui.goal.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.model.SupabaseUser
import com.example.mathapp.domain.repository.SupabaseAuthRepository
import com.example.mathapp.domain.repository.UserGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val supabaseAuthRepository: SupabaseAuthRepository,
    private val goalUserRepository: UserGoalRepository
) : ViewModel() {
    private val _userState = MutableStateFlow(ProfileScreenState())
    val userState = combine(
        _userState,
        goalUserRepository.getAllGoals()
    ) {userState, xyz ->
        var completed: Int = 0
        var ongoing: Int = 0
        xyz.onSuccess {goals ->
           completed = goals.filter { it.isCompleted }.size
           ongoing = goals.filter { !it.isCompleted }.size
        }

        userState.copy(
            completedTasks = completed,
            ongoingTasks = ongoing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileScreenState()
    )

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _userState.update {
                it.copy(isLoading = true, error = null)
            }

            supabaseAuthRepository.loadUserSession().collect { supabaseOperation ->
                supabaseOperation.onSuccess { userSession ->
                    val user = userSession.user
                    user?.let { userInfo ->
                        val name = userInfo.userMetadata?.get("display_name")
                            ?.jsonPrimitive
                            ?.contentOrNull
                            ?: "User"
                        _userState.update {
                            it.copy(
                                isLoading = false,
                                user = SupabaseUser(
                                    userId = userInfo.id,
                                    email = userInfo.email ?: "No email",
                                    name = name
                                )
                            )
                        }
                    }
                }.onFailure { exception ->
                    _userState.update {
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
















