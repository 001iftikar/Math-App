package com.example.mathapp.presentation.studysmart.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.model.Session
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashBoardState())
    val state = combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionDuration()
    ) { state, subjectCount, goalHours, subjects, totalSessionDuration ->
        state.copy(
            totalSubjectCount = subjectCount,
            subjectList = subjects,
            totalGoalStudyHours = goalHours,
            totalStudiedHours = totalSessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashBoardState()
    )

    val tasks: StateFlow<List<Task>> = taskRepository.getAllUpcomingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        ) // “It turns a stream of data into a state that remembers the latest value — and it lives inside the ViewModel.”

    val recentSession: StateFlow<List<Session>> = sessionRepository.getAllSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun onEvent(event: DashBoardEvent) {
        when (event) {
            DashBoardEvent.SaveSubject -> saveSubject()
            is DashBoardEvent.onGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }

            is DashBoardEvent.onSubjectCardColorChange -> {
                _state.update {
                    it.copy(subjectCardColors = event.colors)
                }
            }

            is DashBoardEvent.onSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = event.name)
                }
            }

            is DashBoardEvent.onTaskIsCompleteChange -> {
                updateTask(event.task)
            }

            is DashBoardEvent.onDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }

            DashBoardEvent.DeleteSession -> deleteSession()
        }
    }

    private fun saveSubject() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                subjectRepository.upsertSubject(
                    subject = Subject(
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull()!!,
                        colors = state.value.subjectCardColors.map { it.toArgb() }
                    )
                )
                _state.update {
                    it.copy(
                        subjectName = "",
                        goalStudyHours = ""
                    )
                }
                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Subject Added",
                        duration = SnackbarDuration.Short
                    )
                )
            } catch (e: Exception) {
                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Couldn't save subject. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
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
                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Session deleting failed: ${e.message}",
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
                SnackbarController.sendEvent(
                    SnackbarEvent(message = "Saved in completed tasks.",
                        duration = SnackbarDuration.Short)
                )
            } catch (e: Exception) {
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






























