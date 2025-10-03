package com.iftikar.mathapp.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iftikar.mathapp.domain.model.Session
import com.iftikar.mathapp.domain.model.Subject
import com.iftikar.mathapp.domain.model.Task

@Database(entities = [Subject::class, Task::class, Session::class], version = 3, exportSchema = true, autoMigrations = [
    AutoMigration(from = 1, to = 2),
    AutoMigration(from = 2, to = 3)
])
@TypeConverters(ColorListConverter::class)
abstract class AppLocalDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao

    abstract fun sessionDao(): SessionDao

}