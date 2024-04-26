package com.example.melodydiary.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.melodydiary.model.Diary
import kotlinx.coroutines.flow.Flow
@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_table ")
    suspend fun getAllDiary(): Flow<List<Diary>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Diary)

    @Query("DELETE FROM diary_table")
    suspend fun deleteAll()
}