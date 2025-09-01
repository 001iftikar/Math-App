package com.example.mathapp.ui.goal.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.domain.model.GoalModel
import com.example.mathapp.domain.model.SupabaseUser
import com.example.mathapp.domain.repository.SupabaseAuthRepository
import com.example.mathapp.domain.repository.UserGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
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
    val userState = _userState.asStateFlow()

    private val _eventState: Channel<ProfileScreenEvent> = Channel()
    val evenState = _eventState.receiveAsFlow()

    init {
        getUser()
    }

    fun onEvent(event: ProfileScreenEvent) {
        when(event) {
            ProfileScreenEvent.LogoutClick -> {
                _userState.update {
                    it.copy(alertState = true)
                }
            }
            ProfileScreenEvent.ConfirmClick -> logOut()
            ProfileScreenEvent.DismissRequest -> {
                _userState.update {
                    it.copy(alertState = false)
                }
            }

            else -> Unit
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _userState.update {
                it.copy(isLoading = true, error = null)
            }

            getUserAndGoals().collect { combinedResult ->
                combinedResult.onSuccess { (session, goals) ->
                    val user = session.user
                    val completedTasks = goals.filter { it.isCompleted }.size
                    val ongoingTasks = goals.filter { !it.isCompleted }.size
                    user?.let { userInfo ->
                        val name = userInfo.userMetadata?.get("display_name")
                            ?.jsonPrimitive
                            ?.contentOrNull ?: "User"

                        _userState.update {
                            it.copy(
                                isLoading = false,
                                user = SupabaseUser(
                                    email = userInfo.email ?: "Email not found",
                                    name = name,
                                    completedTasks = completedTasks.toString(),
                                    ongoingTasks = ongoingTasks.toString()
                                )
                            )
                        }
                    }
                }
                    .onFailure { exception ->
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

    private fun getUserAndGoals(): Flow<SupabaseOperation<Pair<UserSession, List<GoalModel>>>> {
        return supabaseAuthRepository.loadUserSession()
            .combine(goalUserRepository.getAllGoals()) { sessionResult, goalsResult ->
                when {
                    sessionResult is SupabaseOperation.Success &&
                            goalsResult is SupabaseOperation.Success -> {
                        SupabaseOperation.Success(sessionResult.data to goalsResult.data)
                    }

                    sessionResult is SupabaseOperation.Failure -> {
                        SupabaseOperation.Failure<Pair<UserSession, List<GoalModel>>>(sessionResult.exception)
                    }

                    goalsResult is SupabaseOperation.Failure -> {
                        SupabaseOperation.Failure<Pair<UserSession, List<GoalModel>>>(goalsResult.exception)
                    }

                    else -> {
                        SupabaseOperation.Failure<Pair<UserSession, List<GoalModel>>>(Exception("Unknown error"))
                    }
                }
            }
    }

    private fun logOut() {
        viewModelScope.launch {
            _userState.update {
                it.copy(isLoading = true, error = null)
            }

            supabaseAuthRepository.signOut().onSuccess {
                _userState.update { it.copy(isLoading = false, alertState = false) }
                this.launch {
                    _eventState.send(ProfileScreenEvent.OnLogout)
                }

            }.onFailure { exception ->
                _userState.update {
                    it.copy(isLoading = false, error = exception.message)
                }
            }
        }
    }
}
















