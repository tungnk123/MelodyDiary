package com.uit.melodydiary.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.uit.melodydiary.model.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDiaryDataSource(
    private val diaryDao: DiaryDao
) {
    suspend fun getDiary(): Flow<List<Diary>> {
        return diaryDao.getAllDiary()
    }

    suspend fun updateDiary(diary: Diary) {
        diaryDao.updateDiary(diary)
    }

    suspend fun getDiaryById(id: Int): Diary {
        return diaryDao.getDiaryById(id)
    }

    suspend fun deleteDiaryById(id: Int) {
        diaryDao.deleteDiaryById(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDiaryAtDate(dateString: String):Flow<List<Diary>> {
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val date = LocalDate.parse(dateString, dateFormatter)
        val startOfDay = date.format(dateFormatter)
        val endOfDay = date.plusDays(1).format(dateFormatter)
        return diaryDao.getDiaryAtDate(startOfDay, endOfDay)
    }

    suspend fun addDiary(diary: Diary) {
        withContext(Dispatchers.IO) {
            diaryDao.insertDiary(diary)
        }

    }

}