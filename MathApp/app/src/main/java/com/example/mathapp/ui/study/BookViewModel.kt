package com.example.mathapp.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.data.ResultState
import com.example.mathapp.domain.model.Book
import com.example.mathapp.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BookViewModel @Inject constructor(private val bookRepository: BookRepository) : ViewModel() {
    private val _booksState = MutableStateFlow(BookResultState())
    val booksState = _booksState.asStateFlow()

    init {
        getAllBooks()
    }

    private fun getAllBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepository.getAllBooks().collect {
                when(it) {
                    is ResultState.Error -> {
                        _booksState.value = _booksState.value.copy(
                            exception = it.exception
                        )
                    }
                    ResultState.Loading -> {
                        _booksState.value = _booksState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success -> {
                        _booksState.value = _booksState.value.copy(
                            isLoading = false,
                            bookList = it.data,
                            exception = null
                        )
                    }
                }
            }

        }
    }
}

data class BookResultState(
    val isLoading: Boolean = false,
    val bookList: List<Book> = emptyList(),
    val exception: Exception? = null

)