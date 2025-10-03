package com.iftikar.mathapp.domain.repository

import com.iftikar.mathapp.data.ResultState
import com.iftikar.mathapp.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooksBySemester(semester: String): Flow<ResultState<List<Book>>>
}