package com.example.mathapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.R

@Composable
fun LoginBackgroundComponent(
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            model = R.drawable.login_background,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}