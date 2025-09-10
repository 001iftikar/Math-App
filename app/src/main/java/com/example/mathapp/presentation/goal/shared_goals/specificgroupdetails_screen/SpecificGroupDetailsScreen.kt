package com.example.mathapp.presentation.goal.shared_goals.specificgroupdetails_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mathapp.domain.model.Group
import com.example.mathapp.presentation.components.GroupBackGroundComponent
import com.example.mathapp.ui.theme.GroupColor1

@Composable
fun SpecificGroupDetailsScreen(
    viewModel: SpecificGroupViewModel
) {
    val state by viewModel.specificGoalState.collectAsStateWithLifecycle()
    Scaffold { innerPadding ->
        GroupBackGroundComponent()

        state.group?.let { group ->
            GroupDetails(
                modifier = Modifier.padding(innerPadding),
                group = group
            )
        }
    }

}

@Composable
private fun GroupDetails(
    modifier: Modifier,
    group: Group
) {
    Column(
        modifier = modifier.fillMaxSize().padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center) {
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
        }

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center) {
            GroupIdCopy(group.id)
        }

    }
}

@Composable
private fun GroupIdCopy(
    groupId: String
) {
    val clipboardManager = LocalClipboardManager.current
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
                    clipboardManager.setText(AnnotatedString(groupId))
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

















