package com.uit.melodydiary.data.repository

import com.uit.melodydiary.data.DiaryDao
import com.uit.melodydiary.network.MusicApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MusicRepository(
    private val apiService: MusicApiService, private val dao: DiaryDao
) {
    suspend fun getMusic(): Flow<String> = flow {
        val musicResponse = apiService.getMusic()
        val musicList = musicResponse.value.musicList
        emit(musicResponse.value.toString())
    }

    suspend fun getGeneratedMusicList(
        emotion: String, genre: String, instrument: String
    ) = apiService.getGeneratedMusicVer2(
        emotion = emotion, genre = genre, instrument = instrument
    )

    fun getAllSmallMusicsByLyric(lyric: String) = dao.getAllMusic()
}