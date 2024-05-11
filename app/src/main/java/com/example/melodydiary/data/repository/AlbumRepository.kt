package com.example.melodydiary.data.repository

import com.example.melodydiary.data.DiaryDao
import com.example.melodydiary.model.Album
import kotlinx.coroutines.flow.Flow

class AlbumRepository(
    val diaryDao: DiaryDao
) {
    suspend fun getAlbum(): Flow<List<Album>> {
        return diaryDao.getAllAlbum()
    }

    suspend fun insertAlbum(album: Album) {
        diaryDao.insertAlbum(album)
    }
}