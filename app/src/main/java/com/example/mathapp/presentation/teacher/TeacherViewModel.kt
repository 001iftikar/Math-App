package com.example.mathapp.presentation.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.ResultState
import com.example.mathapp.domain.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(private val teacherRepository: TeacherRepository) : ViewModel() {
    private val _state = MutableStateFlow(TeacherState())
    val state = _state.asStateFlow()

    init {
        getAllTeachers()
    }

    fun getAllTeachers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(error = null)
            teacherRepository.getAllTeachers().collect {
                when(it) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            teachers = it.data
                        )
                    }

                    is ResultState.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = it.exception.message ?: "Unknown Error!"
                        )
                    }
                }
            }
        }
    }

    fun getTeacherByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.getTeacherByName(name).collect {
                when(it) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            teacher = null,
                            error = ""
                        )
                    }

                    is ResultState.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            teacher = it.data,
                            error = ""
                        )
                    }

                    is ResultState.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = it.exception.message ?: "Something went wrong"
                        )
                    }
                }

            }
        }
    }
}
