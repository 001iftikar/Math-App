package com.example.mathapp.data.repository

import android.util.Log
import com.example.mathapp.data.ResultState
import com.example.mathapp.domain.model.Paper
import com.example.mathapp.domain.repository.PaperRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PaperRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) : PaperRepository {
    override fun getPapers(semester: String): Flow<ResultState<List<Paper>>> = callbackFlow {
        trySend(ResultState.Loading)

        val paperRef = firebaseDatabase.reference
            .child("StudyRes")
            .child("Semester")
            .child(semester)
            .child("Papers")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val paperList = snapshot.children.mapNotNull {
                    it.getValue(Paper::class.java)
                }

                Log.d("Tag-repo-success", paperList.toString())

                trySend(ResultState.Success(paperList))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Tag-repo-error", error.message)
                trySend(ResultState.Error(error.toException()))
            }
        }
        paperRef.addValueEventListener(listener)

        awaitClose { paperRef.removeEventListener(listener) }
        close()
    }
}

































