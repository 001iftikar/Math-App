package com.example.mathapp.presentation.studysmart.task

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.model.Task
import com.example.mathapp.domain.repository.SubjectRepository
import com.example.mathapp.domain.repository.TaskRepository
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
import com.example.mathapp.utils.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val subjectRepository: SubjectRepository,
) : ViewModel() {
    private val taskId: Int = savedStateHandle["taskId"] ?: -1
    private val _state = MutableStateFlow(TaskState())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects()
    ) { state, subjects ->
        state.copy(subjects = subjects)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskState()
    )

    private val _taskEvent: Channel<TaskEvent> = Channel()
    val taskEvent = _taskEvent.receiveAsFlow()

    init {
        fetchTask()
        fetchSubjects()
    }
    fun onEvent(event: TaskEvent) {
        when (event) {
            TaskEvent.DeleteTask -> deleteTask()
            is TaskEvent.OnDateChange -> {
                _state.update {
                    it.copy(
                        dueDate = event.millis
                    )
                }
            }
            is TaskEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(description = event.desc)
                }
            }

            TaskEvent.OnIsCompleteChange -> {
                _state.update {
                    it.copy(
                        isTaskComplete = !_state.value.isTaskComplete
                    )
                }
            }

            is TaskEvent.OnPriorityChange -> {
                _state.update {
                    it.copy(priority = event.priority)
                }
            }
            is TaskEvent.OnRelatedSubjectSelect -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }

            is TaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(title = event.title)
                }
            }

            TaskEvent.SaveTask -> saveTask()

            else -> Unit
        }
    }

    private fun saveTask() {
        viewModelScope.launch(Dispatchers.IO) {

            if (_state.value.subjectId == null || _state.value.relatedToSubject == null) {
                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Please select a subject",
                        duration = SnackbarDuration.Short
                    )
                )
                return@launch
            }

            try {
                taskRepository.upsertTask(
                    Task(
                        taskId = _state.value.currentTaskId,
                        taskSubjectId = _state.value.subjectId!!,
                        title = _state.value.title,
                        description = _state.value.description,
                        dueDate = _state.value.dueDate ?: Instant.now().toEpochMilli(),
                        priority = _state.value.priority.value,
                        relatedToSubject = _state.value.relatedToSubject!!,
                        isComplete = _state.value.isTaskComplete
                    )

                )

                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Task Saved",
                        duration = SnackbarDuration.Short
                    )
                )

                _taskEvent.send(TaskEvent.OnSuccess)

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Saving task was not possible: ${e.message}",
                            duration = SnackbarDuration.Long
                        )
                    )
                }
            }
        }
    }

    private fun fetchTask() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getTaskById(taskId)?.let {task ->
                _state.update {
                    it.copy(
                        title = task.title,
                        description = task.description,
                        dueDate = task.dueDate,
                        isTaskComplete = task.isComplete,
                        priority = Priority.fromInt(task.priority),
                        relatedToSubject = task.relatedToSubject,
                        subjectId = task.taskSubjectId,
                        currentTaskId = task.taskId
                    )
                }
            }
        }
    }

    private fun fetchSubjects() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value.subjectId?.let { subjectId ->
                subjectRepository.getSubjectById(subjectId)?.let { subject ->
                    _state.update {
                        it.copy(
                            relatedToSubject = subject.name,
                            subjectId = subject.subjectId
                        )
                    }
                }
            }
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(taskId)
        }
    }
}
