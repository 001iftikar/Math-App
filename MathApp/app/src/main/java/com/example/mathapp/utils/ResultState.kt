package com.example.mathapp.utils

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error<out T>(val exception: Exception) : ResultState<T>()
    object Loading : ResultState<Nothing>()
}