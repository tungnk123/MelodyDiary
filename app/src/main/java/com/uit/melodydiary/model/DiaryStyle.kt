package com.uit.melodydiary.model


import android.graphics.fonts.FontStyle
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit

data class DiaryStyle(
    val fontSize: TextUnit,
    val fontStyle: String,
    val color: Color,
    val colorPalette: Color
)
