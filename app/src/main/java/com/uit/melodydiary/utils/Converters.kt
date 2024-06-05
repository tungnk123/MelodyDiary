package com.uit.melodydiary.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uit.melodydiary.model.DiaryStyle
import java.time.LocalDateTime


class Converters {
    private val gson = Gson()
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    @TypeConverter
//    fun fromTimestamp(value: String?): LocalDateTime? {
//        return value?.let { LocalDateTime.parse(it) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: LocalDateTime?): String? {
//        return date?.toString()
//    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String {
        return date.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime {
        return LocalDateTime.parse(dateString)
    }

//    @TypeConverter
//    fun fromString(value: String?): Uri? {
//        return if (value == null) null else Uri.parse(value)
//    }
//
//    @TypeConverter
//    fun uriToString(uri: Uri?): String? {
//        return uri?.toString()
//    }

    @TypeConverter
    fun fromDiaryStyle(diaryStyle: DiaryStyle): String {
        return gson.toJson(diaryStyle)
    }

    @TypeConverter
    fun toDiaryStyle(diaryStyleString: String): DiaryStyle {
        return gson.fromJson(diaryStyleString, DiaryStyle::class.java)
    }


}

public operator fun TextUnit.plus(sp: TextUnit): TextUnit {
    val totalPixels = this.value.toInt() + sp.value.toInt()
    return totalPixels.sp
}