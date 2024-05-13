package com.example.melodydiary.model

import androidx.annotation.DrawableRes
import com.example.melodydiary.R

data class MusicSmall(
    val title: String,
    var isPlay: Boolean = false,
    @DrawableRes val logo: Int = R.drawable.ic_music,
    val url: String = "https://un-silent-backend-develop-2.azurewebsites.net/api/v1/musics/file/1UcXwx_lSJdhPVny_EYzpd_nCI93mul5n",
    val duration: Float = 15f
)
