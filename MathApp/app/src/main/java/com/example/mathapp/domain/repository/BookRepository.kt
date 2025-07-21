package com.example.mathapp.domain.repository

import com.example.mathapp.domain.model.Book
import com.example.mathapp.data.ResultState
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooksBySemester(semester: String): Flow<ResultState<List<Book>>>
}