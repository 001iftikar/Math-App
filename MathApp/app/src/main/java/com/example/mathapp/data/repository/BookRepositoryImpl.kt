package com.example.mathapp.data.repository

import com.example.mathapp.domain.model.Book
import com.example.mathapp.domain.repository.BookRepository
import com.example.mathapp.data.ResultState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BookRepositoryImpl @Inject constructor(private  val firebaseDatabase: FirebaseDatabase) : BookRepository {
    override fun getBooksBySemester(semester: String): Flow<ResultState<List<Book>>> = callbackFlow{
        trySend(ResultState.Loading)

        val bookRef = firebaseDatabase.reference
            .child("StudyRes")
            .child("Semester")
            .child(semester)
            .child("Books")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val booksList = snapshot.children.mapNotNull {
                    it.getValue(Book::class.java)
                }
                trySend(ResultState.Success(booksList))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))
            }
        }

        bookRef.addValueEventListener(listener)

        awaitClose { bookRef.removeEventListener(listener) }
        close()

    }
}



















