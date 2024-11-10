package com.uit.melodydiary.data.repository

import com.uit.melodydiary.data.DiaryDao
import com.uit.melodydiary.model.Album
import com.uit.melodydiary.model.MusicSmall
import kotlinx.coroutines.flow.Flow

class AlbumRepository(
    val diaryDao: DiaryDao,
) {
    suspend fun getAlbum(): Flow<List<Album>> {
        return diaryDao.getAllAlbum()
    }

    suspend fun insertAlbum(album: Album) {
        diaryDao.insertAlbum(album)
    }

    suspend fun insertMusic(music: MusicSmall) {
        diaryDao.insertMusic(music)
    }

    fun getMusicFromAlbum(albumId: Int): List<MusicSmall> {
        return diaryDao.getMusicInAlbum(albumId)
    }

    fun getAllMusic(): List<MusicSmall> {
        return diaryDao.getAllMusic()
    }
}