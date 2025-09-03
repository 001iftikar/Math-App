package com.example.mathapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.mathapp.presentation.navigation.NavApp
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.presentation.snackbar.ObserveAsEvents
import com.example.mathapp.presentation.snackbar.SnackbarController
import com.example.mathapp.ui.theme.MathAppTheme
import com.ketch.DownloadConfig
import com.ketch.Ketch
import com.ketch.NotificationConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var ketch: Ketch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        ketch = Ketch.builder()
            .setNotificationConfig(
                NotificationConfig(
                    enabled = true,
                    smallIcon = R.drawable.ic_launcher_background
                )
            )
            .setDownloadConfig(
                DownloadConfig(
                    connectTimeOutInMs = 15000L,
                    readTimeOutInMs = 1500L
                )
            )
            .build(this)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb())
        )
        setContent {
            val navController = rememberNavController()
            MathAppTheme(darkTheme = true, dynamicColor = false) {
                val snackbarHostState = remember {
                    SnackbarHostState()
                }
                val scope = rememberCoroutineScope()
                ObserveAsEvents(
                    flow = SnackbarController.events,
                    snackbarHostState
                ) { event ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val result = snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.name,
                            duration = event.duration
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.route?.let { route ->
                                if (route == Routes.FinishedGoalsScreen) {
                                    navController.navigate(route) {
                                        popUpTo<Routes.GoalHomeScreen> {
                                            inclusive = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavApp(
                        navController,
                        ketch)
                }
            }
        }
    }
}






















