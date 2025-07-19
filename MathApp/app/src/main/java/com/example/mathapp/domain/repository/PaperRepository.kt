package com.example.mathapp.domain.repository

import com.example.mathapp.data.ResultState
import com.example.mathapp.domain.model.Paper
import kotlinx.coroutines.flow.Flow

interface PaperRepository {
    fun getPapers(semester: String): Flow<ResultState<List<Paper>>>
}