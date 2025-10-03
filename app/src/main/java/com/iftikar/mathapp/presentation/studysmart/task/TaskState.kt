package com.iftikar.mathapp.presentation.studysmart.task

import com.iftikar.mathapp.domain.model.Subject
import com.iftikar.mathapp.utils.Priority

data class TaskState(
    val currentTaskId: Int? = null,
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val isTaskComplete: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val relatedToSubject: String? = null,
    val subjects: List<Subject> = emptyList(),
    val subjectId: Int? = null,

)
