package com.example.mathapp.domain.repository

import com.example.mathapp.data.ResultState
import com.example.mathapp.domain.model.Teacher
import kotlinx.coroutines.flow.Flow

interface TeacherRepository {
    fun getAllTeachers(): Flow<ResultState<List<Teacher>>>
    fun getTeacherByName(name: String): Flow<ResultState<Teacher>>
}