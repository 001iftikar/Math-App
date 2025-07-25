package com.example.mathapp.ui.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.AiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(private val aiRepository: AiRepository) : ViewModel() {
    private val _response = MutableStateFlow("")
    val response = _response.asStateFlow()

    fun askQuestion(prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val answer = aiRepository.askGemini(prompt)
            _response.value = answer
        }
    }
}