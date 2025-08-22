package com.example.mathapp.ui.goal.sign_in_up_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.domain.repository.SupabaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupabaseUserCreateViewModel @Inject constructor(
    private val supabaseRepository: SupabaseAuthRepository
) : ViewModel() {
    private val _createUserState = MutableStateFlow<SignUpUserState>(SignUpUserState())
    val createUserState = _createUserState.asStateFlow()

    private val _eventState: Channel<SignUpEvent> = Channel()
    val eventState = _eventState.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when(event) {
            is SignUpEvent.EnterEmail -> {
                _createUserState.update {
                    it.copy(email = event.email)
                }
            }
            is SignUpEvent.EnterName -> {
                _createUserState.update {
                    it.copy(name = event.name)
                }
            }
            is SignUpEvent.EnterPassword -> {
                _createUserState.update {
                    it.copy(password = event.password)
                }
            }
            is SignUpEvent.PasswordVisibilityChange -> {
                _createUserState.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            is SignUpEvent.PasswordError -> {
                if (_createUserState.value.password.length < 6) {
                    _createUserState.update {
                        it.copy(
                            passwordError = event.passwordError
                        )
                    }
                } else {
                    _createUserState.update {
                        it.copy(
                            passwordError = null
                        )
                    }
                }
            }

            is SignUpEvent.EmailEmptyError -> {
                if (_createUserState.value.email.isEmpty()) {
                    _createUserState.update {
                        it.copy(
                            emailEmptyError = event.emailEmptyError
                        )
                    }
                } else {
                    _createUserState.update {
                        it.copy(
                            emailEmptyError = null
                        )
                    }
                }
            }

            is SignUpEvent.PasswordEmptyError -> {
                if (_createUserState.value.password.isEmpty()) {
                    _createUserState.update {
                        it.copy(
                            passwordEmptyError = event.passwordEmptyError
                        )
                    }
                } else {
                    _createUserState.update {
                        it.copy(
                            passwordEmptyError = null
                        )
                    }
                }
            }

            is SignUpEvent.NameEmptyError -> {
                if (_createUserState.value.name.isEmpty()) {
                    _createUserState.update {
                        it.copy(
                            nameEmptyError = event.nameEmptyError
                        )
                    }
                } else {
                    _createUserState.update {
                        it.copy(
                            nameEmptyError = null
                        )
                    }
                }
            }

            else -> {}
        }
    }
    fun signUp() {
        viewModelScope.launch {
            _createUserState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            supabaseRepository.signUp(
                emailValue = _createUserState.value.email,
                passwordValue = _createUserState.value.password,
                name = _createUserState.value.name
            ).collect { supabaseOperation ->
                supabaseOperation.onSuccess {user ->
                    _createUserState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                   viewModelScope.launch(Dispatchers.IO) {
                       _eventState.send(
                           SignUpEvent.Success
                       )
                   }
                }.onFailure { exception ->
                    _createUserState.update {
                        it.copy(
                            error = exception.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _createUserState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            supabaseRepository.signIn(
                emailValue = _createUserState.value.email,
                passwordValue = _createUserState.value.password
            ).collect { supabaseOperation ->
                supabaseOperation.onSuccess { user ->
                    _createUserState.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    viewModelScope.launch(Dispatchers.IO) {
                        _eventState.send(
                            SignUpEvent.Success
                        )
                    }
                }.onFailure { exception ->
                    _createUserState.update {
                        it.copy(
                            error = exception.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}



























