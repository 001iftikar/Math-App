package com.example.mathapp.domain.repository

interface AiRepository {
    suspend fun askGemini(prompt: String): String
}