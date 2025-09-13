package com.example.mathapp.presentation.studysmart.session

import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.model.Subject

data class SessionScreenState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)
