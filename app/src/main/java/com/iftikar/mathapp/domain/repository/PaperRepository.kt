package com.iftikar.mathapp.domain.repository

import com.iftikar.mathapp.data.ResultState
import com.iftikar.mathapp.domain.model.Paper
import kotlinx.coroutines.flow.Flow

interface PaperRepository {
    fun getPapers(semester: String): Flow<ResultState<List<Paper>>>
}