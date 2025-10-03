package com.iftikar.mathapp.domain.repository

import com.iftikar.mathapp.data.ResultState
import com.iftikar.mathapp.domain.model.Teacher
import kotlinx.coroutines.flow.Flow

interface TeacherRepository {
    fun getAllTeachers(): Flow<ResultState<List<Teacher>>>
    fun getTeacherByName(name: String): Flow<ResultState<Teacher>>
}