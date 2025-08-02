package com.example.mathapp.data

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error<out T>(val exception: Exception) : ResultState<T>()
    data object Loading : ResultState<Nothing>()
}