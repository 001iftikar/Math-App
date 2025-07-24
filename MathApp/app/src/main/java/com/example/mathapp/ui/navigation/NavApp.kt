package com.example.mathapp.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mathapp.ui.drawer.ChatBot
import com.example.mathapp.ui.home.HomeScreen
import com.example.mathapp.ui.study.BookPdfViewer
import com.example.mathapp.ui.study.BooksByPaperScreen
import com.example.mathapp.ui.study.StudyHomeScreen
import com.example.mathapp.ui.teacher.TeacherScreen
import com.example.mathapp.ui.teacher.TeacherScreenByName

@Composable
fun NavApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Routes.HomeScreenRoute,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = 165
                    , easing = FastOutSlowInEasing)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = 165
                    , easing = FastOutSlowInEasing)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 165
                    , easing = FastOutSlowInEasing)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 165
                    , easing = FastOutSlowInEasing)
            )
        }
    ) {
        composable<Routes.HomeScreenRoute> {
            HomeScreen(navHostController = navController)
        }

        composable<Routes.TeacherScreenRoute> {
            TeacherScreen(navHostController = navController)
        }

        composable<Routes.TeacherScreenByNameRoute> {
            navBackStackEntry ->
            val name = navBackStackEntry.toRoute<Routes.TeacherScreenByNameRoute>().name
            TeacherScreenByName(teacherName = name, navHostController = navController)
        }

        composable<Routes.StudyScreenRoute> {
            StudyHomeScreen(navHostController = navController)
        }

        composable<Routes.BookByPaperScreen> {
            val data = it.toRoute<Routes.BookByPaperScreen>()
            BooksByPaperScreen(semester = data.semester, paperCode = data.paperCode, navController = navController)
        }

        composable<Routes.ChatBotScreen> {
            ChatBot(navController)
        }

        composable<Routes.PdfViewerScreen> {  
            val data = it.toRoute<Routes.PdfViewerScreen>()
            BookPdfViewer(data.pdfUrl, data.bookName, navController)
        }
    }
}

























