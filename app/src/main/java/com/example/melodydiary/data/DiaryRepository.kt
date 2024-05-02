package com.example.melodydiary.data

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

    suspend fun addDiary(diary: Diary) {
        withContext(Dispatchers.IO) {
            localDiaryDataSource.addDiary(diary)
        }
    }
}