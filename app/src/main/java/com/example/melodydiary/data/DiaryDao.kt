package com.example.melodydiary.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.melodydiary.model.Album
import com.example.melodydiary.model.Diary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_table ")
    fun getAllDiary(): Flow<List<Diary>>


    @Query("SELECT * FROM diary_table WHERE created_at >= :startOfDay AND created_at < :endOfDay")
    fun getDiaryAtDate(startOfDay: String, endOfDay: String): Flow<List<Diary>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDiary(diary: Diary)

    @Query("DELETE FROM diary_table")
    fun deleteAllDiary()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlbum(album: Album)

    @Query("SELECT * FROM album_table ")
    fun getAllAlbum(): Flow<List<Album>>
}