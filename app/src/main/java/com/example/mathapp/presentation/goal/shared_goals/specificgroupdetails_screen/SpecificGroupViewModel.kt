package com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.UseCases
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SpecificGroupViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val groupId: String = checkNotNull(savedStateHandle["groupId"])
    private val _retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _deleteEvent: Channel<Boolean> = Channel()
    val deleteEvent = _deleteEvent.receiveAsFlow()

    fun retry() {
        viewModelScope.launch { _retryTrigger.emit(Unit) }

    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val specificGoalState: StateFlow<SpecificGroupDetailsScreenState> =
        _retryTrigger
            .onStart { emit(Unit) }
            .flatMapLatest {
                combine(
                    useCases.getSpecificGroup(groupId)
                        .timeout(5.seconds)
                        .catch { emit(Result.failure(Exception("Network error"))) },
                    useCases.getGroupMembersForSpecificGroup(groupId)
                        .timeout(5.seconds)
                        .catch { emit(Result.failure(Exception("Network error"))) },
                    useCases.isGroupAdmin(groupId)
                        .timeout(5.seconds)
                        .catch { emit(Result.failure(Exception("Network error"))) }
                ) { groupOp, membersOp, isAdminOp ->
                    SpecificGroupDetailsScreenState(
                        group = groupOp.getOrNull(),
                        belongedMembers = membersOp.getOrNull().orEmpty(),
                        isAdmin = isAdminOp.getOrDefault(false),
                        error = groupOp.exceptionOrNull()?.message
                            ?: membersOp.exceptionOrNull()?.message
                            ?: isAdminOp.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }.onStart {
                    emit(SpecificGroupDetailsScreenState(isLoading = true, error = null))
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SpecificGroupDetailsScreenState(isLoading = true)
            )

    fun deleteGroup() {
        viewModelScope.launch {
            useCases.deleteGroup(groupId)
                .onSuccess {
                    this.launch {
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = "Group deleted",
                                duration = SnackbarDuration.Short
                            )
                        )

                        _deleteEvent.send(true)
                    }
                }.onFailure {
                    this.launch {
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = it.message ?: "Some unexpected error occurred",
                                duration = SnackbarDuration.Long
                            )
                        )
                    }
                }
        }
    }

    fun leaveGroup() {
        viewModelScope.launch {
            useCases.leaveGroup(groupId)
                .onSuccess {
                    this.launch {
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = "Group left",
                                duration = SnackbarDuration.Short
                            )
                        )

                        _deleteEvent.send(true)
                    }
                }.onFailure {
                    this.launch {
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = it.message ?: "Some unexpected error occurred",
                                duration = SnackbarDuration.Long
                            )
                        )
                    }
                }
        }
    }

    fun kickOutMember(groupId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.kickOutMember(groupId, userId)
                .onSuccess {
                    launch(Dispatchers.Main) {
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = "Member is kicked out",
                                duration = SnackbarDuration.Short
                            )
                        )
                    }
                }.onFailure { ex ->
                    launch(Dispatchers.Main) {
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = ex.message ?: "Unexpected error occurred",
                                duration = SnackbarDuration.Long
                            )
                        )
                    }
                }
        }
    }
}


























