package com.example.mathapp.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    // Routes for Study Resources and Teacher Screens
    @Serializable
    object HomeScreenRoute

    @Serializable
    object TeacherScreenRoute

    @Serializable
    data class TeacherScreenByNameRoute(val name: String)

    @Serializable
    object StudyScreenRoute

    @Serializable
    data class BookByPaperScreen(val semester: String, val paperCode: String)

    @Serializable
    data class PdfViewerScreen(val pdfUrl: String, val bookName: String)


    // Routes for Smart Study
    @Serializable
    object StudySmartScreen

    @Serializable
    data class SubjectScreen(val subjectId: Int)
    @Serializable
    object SessionScreen

    @Serializable
    data class TaskScreen(val taskId: Int)
}