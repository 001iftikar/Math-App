package com.example.mathapp.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

object ColorHex {
    fun String.toColor() = Color(this.toColorInt())
}