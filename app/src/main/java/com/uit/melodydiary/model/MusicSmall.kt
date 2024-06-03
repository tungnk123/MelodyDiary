package com.uit.melodydiary.model

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uit.melodydiary.R

@Entity(tableName = "music_table")
data class MusicSmall(
    @PrimaryKey(autoGenerate = true)  val musicId: Int = 0,
    val title: String,
    var isPlay: Boolean = false,
    @DrawableRes val logo: Int = R.drawable.ic_music,
    val url: String = "https://un-silent-backend-develop-2.azurewebsites.net/api/v1/musics/file/1UcXwx_lSJdhPVny_EYzpd_nCI93mul5n",
    val duration: Float = 15f,
    var albumId: Int = 0
)
