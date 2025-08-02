package com.example.mathapp.ui.studysmart.task

import com.example.mathapp.domain.model.Subject
import com.example.mathapp.utils.Priority

data class TaskState(
    val currentTaskId: Int = 0,
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val isTaskComplete: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val relatedToSubject: String? = null,
    val subjects: List<Subject> = emptyList(),
    val subjectId: Int? = null,

)
