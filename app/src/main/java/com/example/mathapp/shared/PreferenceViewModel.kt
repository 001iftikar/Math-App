package com.example.mathapp.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.utils.PreferenceDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferenceViewModel @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) : ViewModel() {
    private val key = "user_name"
    private val _userName = MutableStateFlow<String?>(null)
    val userName = _userName.asStateFlow()

    init {
        getUserName()
    }

    fun setUserName(name: String) {
        viewModelScope.launch {

            try {
                preferenceDataStore.saveUserName(key, name)
                _userName.value = name
            } catch (e: Exception) {
                Log.e("Preference", "setUserName: ${e.message}")
            }
        }
    }

    private fun getUserName() {
        viewModelScope.launch {
            try {
                _userName.value = preferenceDataStore.getUserName(key)
            } catch (e: Exception) {
                Log.e("Preference", "setUserName: ${e.message}")
            }
        }
    }
}












