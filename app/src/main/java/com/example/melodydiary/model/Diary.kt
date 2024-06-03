package com.example.melodydiary.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date

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