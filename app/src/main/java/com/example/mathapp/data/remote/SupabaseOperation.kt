package com.example.mathapp.data.remote

sealed interface SupabaseOperation<T> {
    data class Success<T>(val data: T) : SupabaseOperation<T>
    data class Failure<T>(val exception: Exception) : SupabaseOperation<T>

    fun onSuccess(block: (T) -> Unit): SupabaseOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): SupabaseOperation<T> {
        if (this is Failure) block(exception)
        return this
    }
}