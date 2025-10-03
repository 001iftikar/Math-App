package com.iftikar.mathapp.presentation.studysmart.task

import com.iftikar.mathapp.domain.model.Subject
import com.iftikar.mathapp.utils.Priority

sealed class TaskEvent {
    data object Idle : TaskEvent()
    data class OnTitleChange(val title: String) : TaskEvent()
    data class OnDescriptionChange(val desc: String) : TaskEvent()
    data class OnDateChange(val millis: Long?) : TaskEvent()
    data class OnPriorityChange(val priority: Priority) : TaskEvent()
    data class OnRelatedSubjectSelect(val subject: Subject) : TaskEvent()
    data object OnIsCompleteChange : TaskEvent()
    data object SaveTask : TaskEvent()
    data object DeleteTask : TaskEvent()
    data object OnSuccess : TaskEvent()
}