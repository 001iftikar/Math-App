package com.example.mathapp.ui.teacher

import com.example.mathapp.domain.model.Teacher

data class TeacherState(
    val isLoading: Boolean = false,
    val teachers: List<Teacher> = emptyList(),
    val teacher: Teacher? = null,
    val error: String? = null
)
