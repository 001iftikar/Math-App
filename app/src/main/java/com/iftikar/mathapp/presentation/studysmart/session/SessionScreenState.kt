package com.iftikar.mathapp.presentation.studysmart.session

import com.iftikar.mathapp.domain.model.Session
import com.iftikar.mathapp.domain.model.Subject

data class SessionScreenState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)
