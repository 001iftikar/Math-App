package com.example.mathapp.presentation.studysmart.session

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.repository.SessionRepository
import com.example.mathapp.domain.repository.SubjectRepository
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.presentation.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SessionVIewModel @Inject constructor(
    subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _sessionState = MutableStateFlow(SessionScreenState())
    val sessionState = combine(
        _sessionState,
        subjectRepository.getAllSubjects(),
        sessionRepository.getAllSessions()
    ) { state, subjects, sessions ->
        state.copy(
            subjects = subjects,
            sessions = sessions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
        initialValue = SessionScreenState()
    )

    fun onEvent(event: SessionScreenEvent) {
        when (event) {
            SessionScreenEvent.NotifyToUpdateSubject -> {
                viewModelScope.launch {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Please select a subject",
                            duration = SnackbarDuration.Short
                        )
                    )
                }
            }
            SessionScreenEvent.DeleteSession -> deleteSession()
            is SessionScreenEvent.OnDeleteSessionButtonClick -> {
                _sessionState.update {
                    it.copy(session = event.session)
                }
            }

            is SessionScreenEvent.OnRelatedSubjectChange -> {
                _sessionState.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }

            is SessionScreenEvent.SaveSession -> {
                insertSession(event.duration)
            }

            is SessionScreenEvent.UpdateSubjectIdAndRelatedSubject -> {
                _sessionState.update {
                    it.copy(
                        relatedToSubject = event.relatedToSubject,
                        subjectId = event.subjectId
                    )
                }
            }
        }
    }

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if (duration < 30) {
                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "At least study for more than half a minute, Please!!!",
                        duration = SnackbarDuration.Short
                    )
                )
                return@launch
            }

            try {
                sessionRepository.insertSession(
                    Session(
                        sessionSubjectId = sessionState.value.subjectId,
                        relatedToSubject = sessionState.value.relatedToSubject ?: "",
                        duration = duration,
                        date = Instant.now().toEpochMilli()
                    )
                )

                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Session saved",
                        duration = SnackbarDuration.Short
                    )
                )
            } catch (e: Exception) {
                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Session saving failed: ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                sessionState.value.session?.let {
                    sessionRepository.deleteSession(it)

                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Session deleted successfully",
                            duration = SnackbarDuration.Short
                        )
                    )
                }
            } catch (e: Exception) {
                SnackbarController.sendEvent(
                    SnackbarEvent(
                        message = "Session deleting failed: ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }
}



























