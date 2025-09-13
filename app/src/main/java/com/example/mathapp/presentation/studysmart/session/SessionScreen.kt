package com.example.mathapp.presentation.studysmart.session

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.presentation.components.DeleteDialog
import com.example.mathapp.presentation.components.SubjectListBottomSheet
import com.example.mathapp.presentation.components.studySessionList
import com.example.mathapp.utils.ServiceConstants
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    sessionVIewModel: SessionVIewModel = hiltViewModel(),
    navHostController: NavHostController,
    timerService: StudySessionTimerService
) {
    val context = LocalContext.current
    val hours by timerService.hours
    val minutes by timerService.minutes
    val seconds by timerService.seconds
    val currentTimerState by timerService.currentTimerState


    val sessionState by sessionVIewModel.sessionState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetOpenHandler = { isOpen: Boolean ->
        isSheetOpen = isOpen
    }

    val onEvent = sessionVIewModel::onEvent

    val scope = rememberCoroutineScope()

    var deleteDialogOpenState by remember { mutableStateOf(false) }

    // get the subject after closing the app and coming back by notification
    LaunchedEffect(sessionState.subjects) {
        val subjectId = timerService.subjectId.value
        onEvent(
            SessionScreenEvent.UpdateSubjectIdAndRelatedSubject(
                subjectId = subjectId,
                relatedToSubject = sessionState.subjects.find { it.subjectId == subjectId }?.name
            )
        )
    }

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isSheetOpen,
        subjects = sessionState.subjects,
        onSubjectClicked = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) sheetOpenHandler(false)
            }
            onEvent(SessionScreenEvent.OnRelatedSubjectChange(it))
        },
        onDismissButtonClick = { sheetOpenHandler(false) }
    )

    DeleteDialog(
        isOpen = deleteDialogOpenState,
        title = "Delete Session?",
        bodyText = "Ara you sure? This action cannot be undone!",
        onDismissRequest = {
            deleteDialogOpenState = false
        },
        onConfirmButton = {
            onEvent(SessionScreenEvent.DeleteSession)
            deleteDialogOpenState = false
        }
    )

    Scaffold(
        topBar = {
            SessionScreenTopBar(
                onBackButtonClick = {
                    (context as? Activity)?.onBackPressed()
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds
                )
            }

            item {
                RelatedToSubjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedToSubject = sessionState.relatedToSubject ?: "",
                    seconds = seconds
                ) {
                    sheetOpenHandler(true)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonSection(
                        buttonTitle = when (currentTimerState) {
                            TimerState.IDLE -> "Start"
                            TimerState.STARTED -> "Stop"
                            TimerState.STOPPED -> "Resume"
                        },
                        enabled = true
                    ) {
                        if (sessionState.subjectId != null && sessionState.relatedToSubject != null) {
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = if (currentTimerState == TimerState.STARTED) {
                                    ServiceConstants.ACTION_SERVICE_STOP
                                } else {
                                    ServiceConstants.ACTION_SERVICE_START
                                }
                            )
                            timerService.subjectId.value = sessionState.subjectId
                        } else {
                            onEvent(SessionScreenEvent.NotifyToUpdateSubject)
                        }
                    }

                    ButtonSection(
                        buttonTitle = "Cancel",
                        enabled = seconds != "00" && currentTimerState != TimerState.STARTED
                    ) {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ServiceConstants.ACTION_SERVICE_CANCEL
                        )
                    }

                    ButtonSection(
                        buttonTitle = "Finish",
                        enabled = seconds != "00" && currentTimerState != TimerState.STARTED
                    ) {
                        val duration = timerService.duration.toLong(DurationUnit.SECONDS)
                        if (duration >= 30) {
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = ServiceConstants.ACTION_SERVICE_CANCEL
                            )
                        }

                        onEvent(
                            SessionScreenEvent.SaveSession(
                                duration
                            )
                        )
                    }

                }
            }

            studySessionList(
                sectionTitle = "STUDY SESSIONS HISTORY",
                emptyListText = "I see you haven't studied yet.\n START!!!",
                sessions = sessionState.sessions,
                onDeleteIconClick = {
                    deleteDialogOpenState = true
                    onEvent(SessionScreenEvent.OnDeleteSessionButtonClick(it))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    onBackButtonClick: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                text = "Study Session",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}

@Composable
private fun TimerSection(
    modifier: Modifier,
    hours: String,
    minutes: String,
    seconds: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
        )
        Row {
            AnimatedContent(
                targetState = hours,
                label = hours,
                transitionSpec = { timerTextAnimation() }
            ) {
                Text(
                    text = "$it : ",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }

            AnimatedContent(
                targetState = minutes,
                label = minutes,
                transitionSpec = { timerTextAnimation() }
            ) {
                Text(
                    text = "$it : ",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }

            AnimatedContent(
                targetState = seconds,
                label = seconds,
                transitionSpec = { timerTextAnimation() }
            ) {

                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }


        }

    }
}

@Composable
private fun RelatedToSubjectSection(
    modifier: Modifier,
    relatedToSubject: String,
    seconds: String,
    selectSubjectButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Related to Subject",
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = relatedToSubject,
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = {
                    selectSubjectButtonClick()
                },
                enabled = seconds == "00"
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ButtonSection(
    buttonTitle: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled
    ) {
        Text(text = buttonTitle)
    }
}

private fun timerTextAnimation(duration: Int = 600): ContentTransform {
    return slideInVertically(animationSpec = tween(duration)) { fullHeight -> fullHeight } +
            fadeIn(animationSpec = tween(duration)) togetherWith
            slideOutVertically(animationSpec = tween(duration)) { fullHeight -> -fullHeight } +
            fadeOut(animationSpec = tween(duration))
}



























