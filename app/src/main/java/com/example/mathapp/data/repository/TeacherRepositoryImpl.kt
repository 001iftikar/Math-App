package com.example.mathapp.data.repository

import com.example.mathapp.data.ResultState
import com.example.mathapp.domain.model.Teacher
import com.example.mathapp.domain.repository.TeacherRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeacherRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) : TeacherRepository {
    override fun getAllTeachers(): Flow<ResultState<List<Teacher>>> = callbackFlow {

        trySend(ResultState.Loading)

        val timeoutJob = launch {
            delay(5_000)
            trySend(ResultState.Error(Exception("You are not connected to the internet")))
            close()
        }
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: List<Teacher> = emptyList()
                items = snapshot.children.map {value ->
                    value.getValue(Teacher::class.java)!!
                }
                trySend(ResultState.Success(items))
                timeoutJob.cancel()
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))
                timeoutJob.cancel()
            }

        }


        firebaseDatabase.reference.child("Teachers").addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }
    }

    override fun getTeacherByName(name: String): Flow<ResultState<Teacher>> = callbackFlow {

        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val teacher = snapshot.children
                    .mapNotNull { it.getValue(Teacher::class.java) }
                    .firstOrNull { it.teacherName == name }

                if (teacher != null) {
                    trySend(ResultState.Success(teacher))
                } else {
                    trySend(ResultState.Error(Exception("Teacher not found")))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))
            }

        }

        firebaseDatabase.reference.child("Teachers").addValueEventListener(valueEvent)
        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }
    }

}



















