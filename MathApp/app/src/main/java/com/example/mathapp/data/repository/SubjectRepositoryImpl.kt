package com.example.mathapp.data.repository

import com.example.mathapp.data.local.SessionDao
import com.example.mathapp.data.local.SubjectDao
import com.example.mathapp.data.local.TaskDao
import com.example.mathapp.domain.model.Subject
import com.example.mathapp.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(private val subjectDao: SubjectDao,
    private val taskDao: TaskDao,
    private val sessionDao: SessionDao) : SubjectRepository {
    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    override fun getTotalGoalHours(): Flow<Float> {
        return subjectDao.getTotalGoalHours()
    }

    override suspend fun deleteSubject(subjectId: Int) {
        try {
            subjectDao.deleteSubjectById(subjectId)
            sessionDao.deleteSessionBySubjectId(subjectId)
            taskDao.deleteTaskBySubjectId(subjectId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getSubjectById(subjectId: Int): Subject? {
        return subjectDao.getSubjectById(subjectId)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects()
    }
}