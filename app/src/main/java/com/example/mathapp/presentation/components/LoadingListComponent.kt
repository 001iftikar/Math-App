package com.example.mathapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mathapp.presentation.effects.GoalItemShimmer

@Composable
fun LoadingListComponent(
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(11) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0D0B1E))
            GoalItemShimmer()
            HorizontalDivider(modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0D0B1E))
        }
    }
}
