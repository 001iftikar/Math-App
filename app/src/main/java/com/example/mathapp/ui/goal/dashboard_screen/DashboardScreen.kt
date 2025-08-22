package com.example.mathapp.ui.goal.dashboard_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DashBoardScreen(
) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        GoalItem(modifier = Modifier.padding(innerPadding))
    }

}

@Composable
fun GoalItem(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(6.dp)
            .clickable(onClick = {})
    ) {
        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 50.dp
            )
        ) {
            Text(
                text = "Name",
                modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Descriptionjdfhdshfshfvfn w9fhuvgwrh78dyfyguhoijrdtyfyuhoijpkdtfuygihoijkytcvybnkxrctyvybjk ",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "1 Oct 2025",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

























