package com.example.melodydiary.data

import com.example.melodydiary.model.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDiaryDataSource(
    private val diaryDao: DiaryDao
) {
    suspend fun getDiary():Flow<List<Diary>> {
        return diaryDao.getAllDiary()
    }

    suspend fun addDiary(diary: Diary) {
        withContext(Dispatchers.IO) {
            diaryDao.insertDiary(diary)
        }

    }

}