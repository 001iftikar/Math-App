package com.example.mathapp.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _sharedEventState: Channel<SharedEvent> = Channel()
    val sharedEventState = _sharedEventState.receiveAsFlow()

    fun sendEvent(event: SharedEvent) {
        Log.d("Event", "sendEvent: entering")
        viewModelScope.launch {
            Log.d("Event", "sendEvent: Event sent")
            _sharedEventState.send(event)
        }
    }
}


sealed interface SharedEvent {
    data object Idle : SharedEvent
    data object GroupListModifyEvent : SharedEvent
    data object SharedGoalModify : SharedEvent
}