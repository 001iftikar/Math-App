package com.example.mathapp.domain.repository

import com.example.mathapp.domain.model.Teacher
import com.example.mathapp.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface TeacherRepository {
    fun getAllTeachers(): Flow<ResultState<List<Teacher>>>
    fun getTeacherByName(name: String): Flow<ResultState<Teacher>>
}