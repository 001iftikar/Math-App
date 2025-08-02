package com.example.mathapp.domain.repository

import com.example.mathapp.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun insertSession(session: Session)
   // suspend fun deleteSession(session: Session)
    fun getAllSessions(): Flow<List<Session>>
    fun getTotalSessionDuration(): Flow<Long>

    fun getRecentTenSessionForSubject(subjectId: Int): Flow<List<Session>>
    fun getTotalSessionDurationBySubject(subjectId: Int): Flow<Long>
//
//
   suspend fun deleteSessionBySubjectId(subjectId: Int)
    fun getRecentFiveSessions(): Flow<List<Session>>
}