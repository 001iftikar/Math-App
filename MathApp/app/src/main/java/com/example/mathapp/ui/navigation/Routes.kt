package com.example.mathapp.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    object HomeScreenRoute

    @Serializable
    object TeacherScreenRoute

    @Serializable
    data class TeacherScreenByNameRoute(val name: String)


}