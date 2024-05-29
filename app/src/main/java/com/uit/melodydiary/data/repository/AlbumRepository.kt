package com.uit.melodydiary.data.repository

import com.uit.melodydiary.data.DiaryDao
import com.uit.melodydiary.model.Album
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