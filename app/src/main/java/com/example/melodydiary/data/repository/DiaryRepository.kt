package com.example.melodydiary.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.melodydiary.data.LocalDiaryDataSource
import com.example.melodydiary.data.RemoteDiaryDataSource
import com.example.melodydiary.model.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DiaryRepository(
    private val localDiaryDataSource: LocalDiaryDataSource,
    private val remoteDiaryDataSource: RemoteDiaryDataSource,
) {
    suspend fun getDiary(): Flow<List<Diary>> {
        return localDiaryDataSource.getDiary()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDiaryAtDate(date: String):Flow<List<Diary>> {
        return localDiaryDataSource.getDiaryAtDate(date)
    }

    suspend fun addDiary(diary: Diary) {
        withContext(Dispatchers.IO) {
            localDiaryDataSource.addDiary(diary)
        }
    }
}