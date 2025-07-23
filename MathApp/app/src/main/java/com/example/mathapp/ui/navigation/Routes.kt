package com.example.mathapp.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

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
    object ChatBotScreen

    @Serializable
    data class PdfViewerScreen(val pdfUrl: String, val bookName: String)
}