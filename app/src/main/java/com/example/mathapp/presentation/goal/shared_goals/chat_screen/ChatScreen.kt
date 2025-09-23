package com.example.mathapp.presentation.goal.shared_goals.chat_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SmsFailed
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathapp.data.remote.model.MessageStatus
import com.example.mathapp.domain.model.Message
import com.example.mathapp.presentation.components.GroupBackGroundComponent
import com.example.mathapp.ui.theme.GoalCardColor
import com.example.mathapp.ui.theme.GroupColor
import com.example.mathapp.ui.theme.GroupColor1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    groupInfoClick: (String) -> Unit
) {

    val state = viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent
    if (state.value.currentUserId != null) {

        when {
            state.value.isLoading -> {
                GroupBackGroundComponent()
                LoadingList()
            }

            state.value.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Error(
                        error = state.value.error!!
                    )
                }
            }

            else -> {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(state.value.groupName) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = GroupColor
                            ),
                            actions = {
                                IconButton(
                                    onClick = { groupInfoClick(state.value.groupId) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    GroupBackGroundComponent()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (state.value.messages.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                reverseLayout = true
                            ) {
                                items(state.value.messages) { message ->
                                    ChatBubble(
                                        message = message,
                                        currentUserId = state.value.currentUserId!!
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No messages",
                                    color = GroupColor1
                                )
                            }
                        }

                        SendMessageSection(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.value.textMessage,
                            onValueChange = { onEvent(ChatScreenEvent.OnValueChange(it)) },
                            sendEnabled = state.value.textMessage.isNotEmpty(),
                            onClick = {
                                onEvent(ChatScreenEvent.SendMessage)
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ChatBubble(
    message: Message,
    currentUserId: String
) {
    val isCurrentUser = message.senderId == currentUserId
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isCurrentUser) 40.dp else 4.dp,
                end = if (isCurrentUser) 4.dp else 40.dp,
                top = 4.dp,
                bottom = 4.dp
            ),
        contentAlignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (!isCurrentUser) GoalCardColor else GroupColor1
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(message.senderName, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(3.dp))
                Text(message.content)
                Spacer(Modifier.height(3.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.createdAt,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.width(3.dp))
                    if(isCurrentUser) {
                        val icon = when(message.status) {
                            MessageStatus.SENDING -> Icons.Default.Schedule
                            MessageStatus.SENT -> Icons.Default.Done
                            MessageStatus.FAILED -> Icons.Default.SmsFailed
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SendMessageSection(
    modifier: Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    sendEnabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 4.dp, vertical = 6.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.weight(1f),
            placeholder = {
                Text("Message")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = GoalCardColor,
                unfocusedContainerColor = GoalCardColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true
            )
        )

        IconButton(
            onClick = onClick,
            enabled = sendEnabled
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Send,
                contentDescription = null,
                tint = Color.Green
            )
        }
    }
}

@Composable
private fun LoadingList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = GroupColor1
        )
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    error: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Red
        )
    }
}
































