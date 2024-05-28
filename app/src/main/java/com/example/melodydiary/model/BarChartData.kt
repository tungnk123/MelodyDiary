package com.example.melodydiary.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap


data class BarChartData(
    val icon: ImageBitmap,
    val value: Float,
    val color: Color
)