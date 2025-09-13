package com.example.mathapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.mathapp.presentation.chatbot.ChatBotScreen
import com.example.mathapp.presentation.goal.finished_goals_screen.FinishedGoalsScreen
import com.example.mathapp.presentation.goal.homescreen.GoalsHomeScreen
import com.example.mathapp.presentation.goal.insert_goal_screen.AddGoalScreen
import com.example.mathapp.presentation.goal.ongoing_goals_screen.OngoingGoalsScreen
import com.example.mathapp.presentation.goal.profile_screen.ProfileScreen
import com.example.mathapp.presentation.goal.shared_goals.SharedGoalDashboard
import com.example.mathapp.presentation.goal.shared_goals.addsharedgoal_screen.AddSharedGoalScreen
import com.example.mathapp.presentation.goal.shared_goals.addsharedgoal_screen.AddSharedGoalViewModel
import com.example.mathapp.presentation.goal.shared_goals.creategroup_screen.CreateGroupScreen
import com.example.mathapp.presentation.goal.shared_goals.creategroup_screen.CreateGroupViewModel
import com.example.mathapp.presentation.goal.shared_goals.groups_screen.GroupViewModel
import com.example.mathapp.presentation.goal.shared_goals.groups_screen.GroupsScreen
import com.example.mathapp.presentation.goal.shared_goals.joingroup_screen.JoinGroupScreen
import com.example.mathapp.presentation.goal.shared_goals.joingroup_screen.JoinGroupViewModel
import com.example.mathapp.presentation.goal.shared_goals.sharedgoals_screen.SharedGoalViewModel
import com.example.mathapp.presentation.goal.shared_goals.sharedgoals_screen.SharedGoalsScreen
import com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen.SpecificGroupDetailsScreen
import com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen.SpecificGroupViewModel
import com.example.mathapp.presentation.goal.sign_in_up_screens.GoalSignInScreen
import com.example.mathapp.presentation.goal.sign_in_up_screens.GoalSignUpScreen
import com.example.mathapp.presentation.goal.specific_goal_screen.SpecificGoalScreen
import com.example.mathapp.presentation.home.HomeScreen
import com.example.mathapp.presentation.study.BookPdfViewer
import com.example.mathapp.presentation.study.BooksByPaperScreen
import com.example.mathapp.presentation.study.DownloadsScreen
import com.example.mathapp.presentation.study.StudyHomeScreen
import com.example.mathapp.presentation.studysmart.dashboard.StudySmartScreen
import com.example.mathapp.presentation.studysmart.session.SessionScreen
import com.example.mathapp.presentation.studysmart.session.StudySessionTimerService
import com.example.mathapp.presentation.studysmart.subject.SubjectScreen
import com.example.mathapp.presentation.studysmart.subject.SubjectViewModel
import com.example.mathapp.presentation.studysmart.task.TaskScreen
import com.example.mathapp.presentation.studysmart.task.TaskViewModel
import com.example.mathapp.presentation.teacher.TeacherScreen
import com.example.mathapp.presentation.teacher.TeacherScreenByName
import com.example.mathapp.shared.SharedViewModel
import com.example.mathapp.utils.FAB_EXPLODE_BOUNDS_KEY
import com.example.mathapp.utils.ServiceConstants
import com.ketch.Ketch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavApp(
    navController: NavHostController,
    ketch: Ketch,
    timerService: StudySessionTimerService
) {
    val sharedViewModel = hiltViewModel<SharedViewModel>()
    SharedTransitionLayout {
        NavHost(
            navController = navController, startDestination = Routes.HomeScreenRoute,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = 165, easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = 165, easing = FastOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(
                        durationMillis = 165, easing = FastOutSlowInEasing
                    )
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(
                        durationMillis = 165, easing = FastOutSlowInEasing
                    )
                )
            }
        ) {
            composable<Routes.HomeScreenRoute> {
                HomeScreen(navHostController = navController)
            }

            composable<Routes.TeacherScreenRoute> {
                TeacherScreen(navHostController = navController)
            }

            composable<Routes.TeacherScreenByNameRoute> { navBackStackEntry ->
                val name = navBackStackEntry.toRoute<Routes.TeacherScreenByNameRoute>().name
                TeacherScreenByName(teacherName = name, navHostController = navController)
            }

            composable<Routes.StudyScreenRoute> {
                StudyHomeScreen(navHostController = navController)
            }

            composable<Routes.BookByPaperScreen> {
                val data = it.toRoute<Routes.BookByPaperScreen>()
                BooksByPaperScreen(
                    semester = data.semester,
                    paperCode = data.paperCode,
                    ketch = ketch,
                    navController = navController
                )
            }

            composable<Routes.PdfViewerScreen> {
                val data = it.toRoute<Routes.PdfViewerScreen>()
                BookPdfViewer(
                    pdfUrl = data.pdfUrl,
                    bookName = data.bookName,
                    downloadedFile = data.downloadedPdf,
                    navController = navController
                )
            }

            composable<Routes.StudySmartScreen>(
                deepLinks = listOf(
                    navDeepLink { uriPattern = "yourapp://studysmart" }
                )
            ) {
                StudySmartScreen(navHostController = navController)
            }

            composable<Routes.SessionScreen>(
                deepLinks = listOf(
                    navDeepLink { uriPattern = ServiceConstants.STUDY_SESSION_DEEP_LINK }
                )
            ) {
                SessionScreen(navHostController = navController, timerService = timerService)
            }

            composable<Routes.SubjectScreen> {
                val screenArgs = it.toRoute<Routes.SubjectScreen>()
                val viewModel = hiltViewModel<SubjectViewModel>(
                    key = "subject-${screenArgs.subjectId}" //    "Oh, this is a new key, let's make a new instance of the ViewModel."

//                        That means:
//
//                Navigating to different subject IDs = different ViewModels
                )
                SubjectScreen(
                    subjectViewModel = viewModel,
                    navController = navController,
                    subjectId = screenArgs.subjectId
                )
            }

            composable<Routes.TaskScreen> {

                val taskScreenArgs = it.toRoute<Routes.TaskScreen>()
                val viewModel = hiltViewModel<TaskViewModel>(
                    key = "task-${taskScreenArgs.taskId}"
                )
                TaskScreen(taskViewModel = viewModel, navController = navController)
            }

            composable<Routes.ChatBotScreen> {
                ChatBotScreen()
            }

            composable<Routes.DownloadsScreen> {
                DownloadsScreen(navController)
            }

            composable<Routes.GoalSignUpScreen> {
                GoalSignUpScreen(navHostController = navController)
            }

            composable<Routes.GoalSignInScreen> {
                GoalSignInScreen(navHostController = navController)
            }

            composable<Routes.GoalHomeScreen> {
                GoalsHomeScreen(
                    navHostController = navController
                )
            }

            composable<Routes.UnfinishedGoalScreen> {
                OngoingGoalsScreen(
                    navHostController = navController,
                    animatedVisibilityScope = this,
                )
            }

            composable<Routes.AddGoalScreen> {
                AddGoalScreen(
                    modifier = Modifier
                        .background(Color(0xFF0D0B1E))
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = FAB_EXPLODE_BOUNDS_KEY
                            ),
                            animatedVisibilityScope = this
                        ),
                    navHostController = navController
                )
            }

            composable<Routes.SpecificGoalScreen> {
                val goalId = it.toRoute<Routes.SpecificGoalScreen>().goalId
                SpecificGoalScreen(goalId = goalId, navHostController = navController)
            }

            composable<Routes.FinishedGoalsScreen> {
                FinishedGoalsScreen(
                    navHostController = navController
                )
            }

            composable<Routes.ProfileScreen> {
                ProfileScreen(navHostController = navController)
            }

            composable<Routes.SharedDashboard> {
                SharedGoalDashboard(navController)
            }

            composable<Routes.GroupsScreen> {
                val viewModel = hiltViewModel<GroupViewModel>()
                GroupsScreen(
                    sharedViewModel = sharedViewModel,
                    viewModel = viewModel,
                    goToSharedGoalsScreen = { id, name ->
                        navController.navigate(Routes.SharedGoalsScreen(id, name))
                    },
                    goToCreateGroupScreen = { navController.navigate(Routes.CreateGroupScreen) }
                )
            }

            composable<Routes.CreateGroupScreen> {
                val viewModel = hiltViewModel<CreateGroupViewModel>()
                CreateGroupScreen(
                    sharedViewModel = sharedViewModel,
                    viewModel = viewModel,
                    onCreateSuccess = { navController.popBackStack() }
                )
            }

            composable<Routes.JoinGroupScreen> {
                val viewModel = hiltViewModel<JoinGroupViewModel>()
                JoinGroupScreen(
                    viewModel = viewModel,
                    onSuccess = { navController.popBackStack() }
                )
            }

            composable<Routes.SharedGoalsScreen> { navBackStack ->
                val viewModel = hiltViewModel<SharedGoalViewModel>()

                SharedGoalsScreen(
                    sharedViewModel = sharedViewModel,
                    viewModel = viewModel,
                    groupInfoClick = { navController.navigate(Routes.SpecificGroupDetailsScreen(it)) },
                    addGoal = { navController.navigate(Routes.AddSharedGoalScreen(it)) },
                )
            }

            composable<Routes.AddSharedGoalScreen> { navBackStack ->
                val viewModel = hiltViewModel<AddSharedGoalViewModel>()
                val groupId = navBackStack.toRoute<Routes.SharedGoalsScreen>().groupId
                AddSharedGoalScreen(
                    sharedViewModel = sharedViewModel,
                    viewModel = viewModel,
                    groupId = groupId,
                    onCancel = { navController.popBackStack() },
                    backOnSuccess = { navController.popBackStack() }
                )
            }

            composable<Routes.SpecificGroupDetailsScreen> {
                val viewModel = hiltViewModel<SpecificGroupViewModel>()
                SpecificGroupDetailsScreen(
                    viewModel
                )
            }
        }
    }
}
