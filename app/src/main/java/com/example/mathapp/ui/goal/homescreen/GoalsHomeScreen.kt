package com.example.mathapp.ui.goal.homescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.R
import com.example.mathapp.ui.components.BlackBackGround
import com.example.mathapp.ui.navigation.Routes
import com.example.mathapp.ui.theme.GoalCardColor

@Composable
fun GoalsHomeScreen(
    navHostController: NavHostController
) {
        Scaffold { innerPadding->
            BlackBackGround {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    FirstLayer(
                        onFirstItemClick = {
                            navHostController.navigate(Routes.UnfinishedGoalScreen)
                        },
                        onSecondItemClick = {}
                    )

                    Spacer(Modifier.height(8.dp))
                    SecondLayer(
                        onFirstItemClick = {navHostController.navigate(Routes.ProfileScreen)}
                    )
                }
            }
        }
}

@Composable
private fun FirstLayer(
    onFirstItemClick: () -> Unit,
    onSecondItemClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HomeScreenItem(
            image = R.drawable.unfinished,
            title = "Ongoing Goals",
            onClick = onFirstItemClick
        )

        HomeScreenItem(
            image = R.drawable.finished,
            title = "Achieved",
            onSecondItemClick
        )
    }
}

@Composable
private fun SecondLayer(
    onFirstItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HomeScreenItem(
            image = R.drawable.profile,
            title = "Profile",
            onClick = onFirstItemClick
        )
    }
}

@Composable
private fun HomeScreenItem(
    image: Int,
    title: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .size(160.dp)
        ,
        colors = CardDefaults.cardColors(
            containerColor = GoalCardColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clickable(
                        onClick = onClick
                    )

            )
            Text(
                text = title,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        onClick = onClick
                    ),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}