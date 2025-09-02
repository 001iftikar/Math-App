package com.example.mathapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun BlackBackGround(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black)
    ) {
        content()
    }
}