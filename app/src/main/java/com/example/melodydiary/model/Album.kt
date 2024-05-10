package com.example.melodydiary.model

import androidx.annotation.DrawableRes

data class Album(
    val title: String,
    val description: String,
    @DrawableRes val logo: Int,
    val count: Int = 0
)
