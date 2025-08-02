package com.example.mathapp.ui.studysmart.subject

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.model.Subject
import com.example.mathapp.domain.repository.SessionRepository
import com.example.mathapp.domain.repository.SubjectRepository
import com.example.mathapp.domain.repository.TaskRepository
import com.example.mathapp.utils.SnackBarEvent
import com.example.mathapp.utils.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val subjectRepository: SubjectRepository,
    taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {
    private val subjectId: Int = checkNotNull(savedStateHandle["subjectId"])
    private val _state = MutableStateFlow(SubjectState())
    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(subjectId),
        taskRepository.getCompletedTasksForSubject(subjectId),
        sessionRepository.getRecentTenSessionForSubject(subjectId),
        sessionRepository.getTotalSessionDurationBySubject(subjectId)
    ) { state, upComingTasks, completedTasks, recentSessions, totalSessionDuration ->
        state.copy(
            upcomingTasks = upComingTasks,
            completedTasks = completedTasks,
            recentSessions = recentSessions,
            studiedHours = totalSessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SubjectState()
    )

    private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        fetchSubject()
    }

    fun onEvent(event: SubjectEvent) {
        when (event) {
            SubjectEvent.DeleteSession -> deleteSession()
            SubjectEvent.DeleteSubject -> deleteSubject()
            is SubjectEvent.OnDeleteSessionButtonClick -> TODO()
            is SubjectEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(
                        goalStudyHours = event.hours
                    )
                }
            }

            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColors = event.color
                    )
                }
            }

            is SubjectEvent.OnSubjectNameChange ->
                _state.update {
                    it.copy(
                        subjectName = event.name
                    )
                }

            is SubjectEvent.OnTaskIsCompleteChange -> TODO()
            SubjectEvent.UpdateSubject -> {
                updateSubject()
            }

            SubjectEvent.UpdateProgress -> {
                val goalStudyHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)
                    )
                }
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            subjectRepository.getSubjectById(subjectId)?.let { subject ->
                _state.update {
                    it.copy(
                        subjectName = subject.name,
                        goalStudyHours = subject.goalHours.toString(),
                        subjectCardColors = subject.colors.map { color -> Color(color) },
                        currentSubjectId = subject.subjectId
                    )
                }
            }
        }
    }

    private fun deleteSubject() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentSubjectId = state.value.currentSubjectId
                if (currentSubjectId != null) {
                    subjectRepository.deleteSubject(currentSubjectId)


                    _snackBarEvent.emit(
                        SnackBarEvent.ShowSnackBar(
                            message = "Subject deleted successfully",
                        )
                    )
                    delay(350)
                    _snackBarEvent.emit(
                        SnackBarEvent.NavigateBack
                    )
                } else {
                    _snackBarEvent.emit(
                        SnackBarEvent.ShowSnackBar(
                            message = "No subject",
                        )
                    )
                }
            } catch (e: Exception) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Error deleting subject: ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun deleteSession() {

    }

    private fun updateSubject() {
        viewModelScope.launch {
            try {
                state.value.currentSubjectId?.let {
                    subjectRepository.upsertSubject(
                        subject = Subject(
                            subjectId = it,
                            name = state.value.subjectName,
                            goalHours = state.value.goalStudyHours.toFloat(),
                            colors = state.value.subjectCardColors.map { color -> color.toArgb() },
                        )
                    )
                    _snackBarEvent.emit(
                        SnackBarEvent.ShowSnackBar(message = "Subject updated")
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Error updating subject: ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }
        }
    }
}


























