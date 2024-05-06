package com.example.melodydiary.model

import androidx.annotation.DrawableRes
import com.example.melodydiary.R

data class Music(
    val title: String,
    val isPlay: Boolean = false,
    @DrawableRes val logo: Int = R.drawable.ic_music
)
