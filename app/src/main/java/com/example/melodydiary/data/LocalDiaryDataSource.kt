package com.example.melodydiary.data

import com.example.melodydiary.model.Diary
import kotlinx.coroutines.flow.Flow

class LocalDiaryDataSource(
    private val diaryDao: DiaryDao
) {
}