package com.uit.melodydiary.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.uit.melodydiary.data.LocalDiaryDataSource
import com.uit.melodydiary.data.RemoteDiaryDataSource
import com.uit.melodydiary.model.Diary
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

    suspend fun updateDiary(diary: Diary) {
        withContext(Dispatchers.IO) {
            localDiaryDataSource.updateDiary(diary)
        }
    }

    suspend fun getDiaryById(id: Int): Diary {
        return localDiaryDataSource.getDiaryById(id)
    }

    suspend fun deleteDiaryById(id: Int) {
        localDiaryDataSource.deleteDiaryById(id)
    }
}