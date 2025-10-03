package com.iftikar.mathapp.presentation.teacher

import com.iftikar.mathapp.domain.model.Teacher

data class TeacherState(
    val isLoading: Boolean = false,
    val teachers: List<Teacher> = emptyList(),
    val teacher: Teacher? = null,
    val error: String? = null
)
