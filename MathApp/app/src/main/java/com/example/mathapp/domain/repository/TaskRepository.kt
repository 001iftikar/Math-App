package com.example.mathapp.domain.repository

import com.example.mathapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun upsertTask(task: Task)
    suspend fun deleteTask(taskId: Int)
    suspend fun deleteTaskBySubjectId(subjectId: Int)
    suspend fun getTaskById(taskId: Int): Task?
    fun getTasksForSubject(subjectId: Int): Flow<List<Task>>
    fun getAllTasks(): Flow<List<Task>>
    fun getAllUpcomingTasks(): Flow<List<Task>>
}