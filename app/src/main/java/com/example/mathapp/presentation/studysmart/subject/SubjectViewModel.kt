package com.example.mathapp.presentation.studysmart.subject

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.model.Subject
import com.example.mathapp.domain.model.Task
import com.example.mathapp.domain.repository.SessionRepository
import com.example.mathapp.domain.repository.SubjectRepository
import com.example.mathapp.domain.repository.TaskRepository
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
import com.example.mathapp.utils.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
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

    init {
        fetchSubject()
    }

    fun onEvent(event: SubjectEvent) {
        when (event) {
            SubjectEvent.DeleteSession -> deleteSession()
            SubjectEvent.DeleteSubject -> deleteSubject()
            is SubjectEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(
                        session = event.session
                    )
                }
            }
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

            is SubjectEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(
                        subjectName = event.name
                    )
                }
            }

            is SubjectEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }

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
        viewModelScope.launch(Dispatchers.IO) {
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


                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Subject deleted successfully",
                            duration = SnackbarDuration.Short
                        )
                    )
                } else {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "No subject",
                            duration = SnackbarDuration.Short
                        )
                    )
                }
            } catch (e: Exception) {
                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Error deleting subject: ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
                if (task.isComplete) {
                    SnackbarController.sendEvent(
                        SnackbarEvent(message = "Saved in upcoming tasks.",
                            duration = SnackbarDuration.Short)
                    )
                } else {
                    SnackbarController.sendEvent(
                        SnackbarEvent(message = "Saved in completed tasks.",
                            duration = SnackbarDuration.Short)
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Couldn't update task. ${e.message}",
                            duration = SnackbarDuration.Long
                        )
                    )
                }
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                state.value.session?.let {
                    sessionRepository.deleteSession(it)

                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Session deleted successfully",
                            duration = SnackbarDuration.Short
                        )
                    )
                }
            } catch (e: Exception) {
               withContext(Dispatchers.Main) {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Session deleting failed: ${e.message}",
                            duration = SnackbarDuration.Long
                        )
                    )
                }
            }
        }
    }

    private fun updateSubject() {
        viewModelScope.launch(Dispatchers.IO) {
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
                    SnackbarController.sendEvent(
                        SnackbarEvent(message = "Subject updated",
                            duration = SnackbarDuration.Short)
                    )
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Error updating subject: ${e.message}",
                            duration = SnackbarDuration.Long
                        )
                    )
                }
            }
        }
    }
}


























