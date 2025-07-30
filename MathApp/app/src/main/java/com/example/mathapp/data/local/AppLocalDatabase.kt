package com.example.mathapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.model.Subject
import com.example.mathapp.domain.model.Task

@Database(entities = [Subject::class, Task::class, Session::class], version = 1, exportSchema = false)
@TypeConverters(ColorListConverter::class)
abstract class AppLocalDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao

    abstract fun sessionDao(): SessionDao
}