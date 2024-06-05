package com.uit.melodydiary.model

import android.graphics.fonts.FontStyle
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "diary_table")
data class Diary(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "diary_id") val diaryId: Int,
    var title: String,
    var content: String,
    val mood: String,
    val imageIdList: List<String>,
    @DrawableRes val logo: Int,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    val diaryStyle: DiaryStyle = DiaryStyle(
        fontSize = 16.sp,
        fontStyle = "Default",
        color = Color.Black
        )
    )
