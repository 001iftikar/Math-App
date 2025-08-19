package com.example.mathapp.data.repository

import android.util.Log
import com.example.mathapp.data.ResultState
import com.example.mathapp.data.SupabaseOperation
import com.example.mathapp.domain.model.SupabaseUser
import com.example.mathapp.domain.repository.SupabaseRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import okio.IOException
import javax.inject.Inject

class SupabaseRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : SupabaseRepository {
    override fun signUp(
        emailValue: String,
        passwordValue: String,
        name: String
    ): Flow<SupabaseOperation<SupabaseUser>> = flow {

        try {
            val user = supabaseClient.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
                data = buildJsonObject {
                    put("display_name", name)
                }
            }
            val userId = user?.id
            val displayName = user?.userMetadata?.get("display_name")
                ?.jsonPrimitive
                ?.contentOrNull
            if (userId != null) {
                emit(
                    SupabaseOperation.Success(
                        data = SupabaseUser(
                            userId = userId,
                            email = emailValue,
                            name = displayName ?: "User"
                        )
                    )
                )
            } else {
                emit(SupabaseOperation.Failure(NullPointerException("User")))
            }


        } catch (e: IOException) {
            Log.e("Supabase-Repo", "signUp: IO Exception: ${e.localizedMessage}")
            emit(SupabaseOperation.Failure(Exception("Please check your internet connection")))
        } catch (e: Exception) {
            Log.e("Supabase-Repo", "signUp: Exception: ${e.localizedMessage}")
            when {
                e.localizedMessage?.contains("user_already_exists", ignoreCase = true) == true -> {
                    emit(SupabaseOperation.Failure(Exception("This email is already registered.")))
                }

                e.localizedMessage?.contains("invalid_credentials", ignoreCase = true) == true -> {
                    emit(SupabaseOperation.Failure(Exception("Invalid email or password.")))
                }
                else -> {
                    emit(SupabaseOperation.Failure(Exception("Unexpected error: ${e.localizedMessage}")))
                }
            }
        }
        }
    }

//    override fun signIn(
//        emailValue: String,
//        passwordValue: String
//    ): Flow<ResultState<String>> = flow {
//        try {
//            val result = supabaseClient.auth.signUpWith(Email) {
//                email = emailValue
//                password = passwordValue
//            }
//            val userId = result?.id
//            if (userId != null) {
//                emit(ResultState.Success(userId))
//            } else {
//                emit(ResultState.Error(NullPointerException("Sign in not successful")))
//            }
//        } catch (e: IOException) {
//            Log.e("Supabase-Repo", "signUp: IO Exception: ${e.localizedMessage}")
//            emit(ResultState.Error(IOException("Network error, please check your connection.")))
//        } catch (e: Exception) {
//            Log.e("Supabase-Repo", "signUp: Exception: ${e.localizedMessage}")
//
//            when {
//                e.localizedMessage?.contains("user_already_exists", ignoreCase = true) == true -> {
//                    emit(ResultState.Error(Exception("This email is already registered.")))
//                }
//
//                e.localizedMessage?.contains("invalid_credentials", ignoreCase = true) == true -> {
//                    emit(ResultState.Error(Exception("Invalid email or password.")))
//                }
//
//                else -> {
//                    emit(ResultState.Error(Exception("Unexpected error: ${e.localizedMessage}")))
//                }
//            }
//        }
//    }
