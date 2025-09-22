package com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SpecificGroupViewModel @Inject constructor(
    useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val groupId: String = checkNotNull(savedStateHandle["groupId"])
    private val _retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

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
}


























