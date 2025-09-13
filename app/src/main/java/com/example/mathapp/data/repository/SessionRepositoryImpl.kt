package com.example.mathapp.data.repository

import com.example.mathapp.data.local.SessionDao
import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(private val sessionDao: SessionDao) : SessionRepository {
    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }

    override fun getAllSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions().map { sessions ->
            sessions.sortedByDescending { it.date } // latest first
        }
    }

    override fun getTotalSessionDuration(): Flow<Long> {
        return sessionDao.getTotalSessionDuration()
    }

    override fun getRecentTenSessionForSubject(subjectId: Int): Flow<List<Session>> {
        return sessionDao.getRecentSessionForSubject(subjectId)
            .map { sessions ->
                sessions.sortedByDescending { it.date }
            }
            .take(10)
    }

    override fun getTotalSessionDurationBySubject(subjectId: Int): Flow<Long> {
        return sessionDao.getTotalSessionDurationBySubject(subjectId)
    }

    override suspend fun deleteSessionBySubjectId(subjectId: Int) {
        sessionDao.deleteSessionBySubjectId(subjectId)
    }

    override fun getRecentFiveSessions(): Flow<List<Session>> {
        return sessionDao.getRecentFiveSessions().map { sessions ->
            sessions.sortedByDescending { it.date }
        }
    }
}