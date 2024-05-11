package com.example.melodydiary.model

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_table")
data class Album(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "album_id") val albumId: Int,
    val title: String,
    val description: String,
    @ColumnInfo(name = "logo", typeAffinity = ColumnInfo.BLOB)
    val logo : ByteArray,
    val count: Int = 0
)
