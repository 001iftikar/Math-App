package com.example.mathapp.data.repository

import com.example.mathapp.domain.repository.AiRepository
import com.google.firebase.ai.GenerativeModel
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(private val model: GenerativeModel) : AiRepository {
    override suspend fun askGemini(prompt: String): String {
        return try {
            val result = model.generateContent(prompt)
            result.text ?: "No response"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}