package com.uit.melodydiary.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uit.melodydiary.model.Album
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.model.MusicSmall
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_table ")
    fun getAllDiary(): Flow<List<Diary>>


    @Query("SELECT * FROM diary_table WHERE created_at >= :startOfDay AND created_at < :endOfDay")
    fun getDiaryAtDate(startOfDay: String, endOfDay: String): Flow<List<Diary>>


    @Query("SELECT * FROM diary_table WHERE diary_id = :diaryId")
    fun getDiaryById(diaryId: Int): Diary

    @Query("DELETE FROM diary_table WHERE diary_id = :id")
    suspend fun deleteDiaryById(id: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDiary(diary: Diary)

    @Update
    suspend fun updateDiary(diary: Diary)

    @Query("DELETE FROM diary_table")
    fun deleteAllDiary()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlbum(album: Album)

    @Query("SELECT * FROM album_table ")
    fun getAllAlbum(): Flow<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicSmall)

    @Query("SELECT * FROM music_table ")
    fun getAllMusic(): List<MusicSmall>

    @Query("SELECT * FROM music_table WHERE emotion = :emotion")
    fun getAllMusicByEmotion(emotion: String): List<MusicSmall>

    @Query("SELECT * FROM music_table WHERE albumId =:albumId ")
    fun getMusicInAlbum(albumId: Int): List<MusicSmall>
}