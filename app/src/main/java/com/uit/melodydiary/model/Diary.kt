package com.uit.melodydiary.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.uit.melodydiary.ui.theme.mygreen
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
        colorPalette = mygreen
        )
    )
