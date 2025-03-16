package com.uit.melodydiary.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uit.melodydiary.ui.theme.mauDa7
import java.time.LocalDateTime

@Entity(tableName = "diary_table")
data class Diary(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "diary_id") val diaryId: Int,
    var title: String,
    var content: String,
    val mood: String,
    var contentFilePath: String,
    @DrawableRes val logo: Int,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    val diaryStyle: DiaryStyle = DiaryStyle(
        fontSize = 16.sp,
        fontStyle = "Default",
        color = Color.Black,
        colorPalette = mauDa7
    ),
    val songPath: String = "https://un-silent-backend-mobile.azurewebsites.net/api/v1/musics/file/1QFxYBQiVS1KY2fkr9vD76H6N4MwON4rL",
)
