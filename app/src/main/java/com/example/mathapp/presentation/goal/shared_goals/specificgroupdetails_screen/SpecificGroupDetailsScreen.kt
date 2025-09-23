package com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen

import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.domain.model.Group
import com.example.mathapp.domain.model.UserProfile
import com.example.mathapp.presentation.components.DeleteDialog
import com.example.mathapp.presentation.components.GroupBackGroundComponent
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.ui.theme.GroupColor1
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SpecificGroupDetailsScreen(
    viewModel: SpecificGroupViewModel,
    navHostController: NavHostController
) {
    val state by viewModel.specificGoalState.collectAsStateWithLifecycle()
    var isDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.deleteEvent.collectLatest {
            if (it) {
                navHostController.navigate(Routes.GroupsScreen) {
                    popUpTo<Routes.GroupsScreen> {
                        inclusive = true
                    }
                }
            }
        }
    }
    Scaffold { innerPadding ->
        GroupBackGroundComponent()

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GroupColor1)
                }
            }

            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.error!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = { viewModel.retry() },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = GroupColor1
                        )
                    ) {
                        Text("Refresh")
                    }
                }
            }

            state.group != null -> {
                GroupDetails(
                    modifier = Modifier.padding(innerPadding),
                    group = state.group!!,
                    isAdmin = state.isAdmin,
                    members = state.belongedMembers,
                    onDeleteClick = { isDialogOpen = true }
                )

                DeleteDialog(
                    isOpen = isDialogOpen,
                    title = "Delete the group?",
                    bodyText = "Careful with this one, deleting will erase all the goals this group owns",
                    onDismissRequest = { isDialogOpen = false },
                    onConfirmButton = {
                        isDialogOpen = false
                        viewModel.deleteGroup()
                    }
                )
            }
        }
    }
}

@Composable
private fun GroupDetails(
    modifier: Modifier,
    group: Group,
    isAdmin: Boolean,
    members: List<UserProfile>,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = GroupColor1.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = group.description ?: "",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp),
                fontStyle = FontStyle.Italic
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = group.createdAt,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp
                )

                if (isAdmin) {
                    IconButton(
                        onClick = onDeleteClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            }

        }

        Spacer(Modifier.height(8.dp))

        if (isAdmin) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GroupIdCopy(group.id)
            }

            Spacer(Modifier.height(30.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = GroupColor1.copy(alpha = 0.3f)
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                item {
                    Text(
                        text = "${members.size} members",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(Modifier.height(8.dp))
                }
                items(items = members) { member ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = member.email,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupIdCopy(
    groupId: String
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    val clipData = ClipData.newPlainText("group Id", groupId)
    val clipEntry = clipData.toClipEntry()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(groupId)
            IconButton(
                onClick = {
                    scope.launch {
                        clipboard.setClipEntry(clipEntry)
                    }

                }
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null
                )
            }
        }
    }
}

















