package com.example.mathapp.presentation.studysmart.session

import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.model.Subject

sealed class SessionScreenEvent {
    data class OnRelatedSubjectChange(val subject: Subject) : SessionScreenEvent()
    data class SaveSession(val duration: Long) : SessionScreenEvent()
    data class OnDeleteSessionButtonClick(val session: Session) : SessionScreenEvent()
    data object DeleteSession : SessionScreenEvent()
    data object NotifyToUpdateSubject : SessionScreenEvent()
    data class UpdateSubjectIdAndRelatedSubject(
        val subjectId: Int?,
        val relatedToSubject: String?
    ) : SessionScreenEvent()
}