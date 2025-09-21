package com.example.mathapp.presentation.navigation

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
    data class PdfViewerScreen(val pdfUrl: String? = null, val downloadedPdf: String? = null, val bookName: String)

    @Serializable
    object DownloadsScreen


    // Routes for Smart Study
    @Serializable
    object StudySmartScreen

    @Serializable
    data class SubjectScreen(val subjectId: Int)
    @Serializable
    object SessionScreen

    @Serializable
    data class TaskScreen(val taskId: Int?)

    // Routes for Ai
    @Serializable
    object ChatBotScreen

    // Routes for Goals
    @Serializable
    object GoalHomeScreen
    @Serializable
    object GoalSignUpScreen

    @Serializable
    object GoalSignInScreen

    @Serializable
    object UnfinishedGoalScreen

    @Serializable
    object AddGoalScreen

    @Serializable
    data class SpecificGoalScreen(val goalId: String)

    @Serializable
    data object ProfileScreen

    @Serializable
    data object FinishedGoalsScreen

    @Serializable
    data object SharedDashboard
    @Serializable
    data object GroupsScreen
    @Serializable
    data object CreateGroupScreen
    @Serializable
    data object JoinGroupScreen
    @Serializable
    data class SharedGoalsScreen(val groupId: String, val groupName: String = "") // had to give a default value cuz it was crashing when clicking on add shared goal button
    @Serializable
    data class AddSharedGoalScreen(val groupId: String)
    @Serializable
    data class SpecificGroupDetailsScreen(val groupId: String)
    @Serializable
    data class ChatScreen(val groupId: String, val groupName: String)
}