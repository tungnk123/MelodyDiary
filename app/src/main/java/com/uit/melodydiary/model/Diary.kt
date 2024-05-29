package com.uit.melodydiary.model

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "diary_table")
data class Diary(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "diary_id") val diaryId: Int,
    val title: String,
    val content: String,
    val mood: String,
    val imageIdList: List<String>,
    @DrawableRes val logo: Int,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    )
