package com.example.mathapp.presentation.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.ResultState
import com.example.mathapp.domain.model.Paper
import com.example.mathapp.domain.repository.PaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaperViewModel @Inject constructor(private val paperRepository: PaperRepository) : ViewModel() {
    private val _paperState = MutableStateFlow(PaperResultState())
    val paperState = _paperState.asStateFlow()

    fun getPapers(semester: String) {
        viewModelScope.launch(Dispatchers.IO) {
            paperRepository.getPapers(semester).collect {
                when(it) {
                    is ResultState.Error -> {
                        _paperState.value = _paperState.value.copy(
                            exception = it.exception
                        )
                    }
                    ResultState.Loading -> {
                        _paperState.value = _paperState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success -> {
                        _paperState.value = _paperState.value.copy(
                            isLoading = false,
                            papers = it.data,
                            exception = null

                        )
                    }
                }
            }
        }
    }
}


data class PaperResultState(
    val isLoading: Boolean = false,
    val papers: List<Paper> = emptyList(),
    val exception: Exception? = null
)