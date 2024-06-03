package com.uit.melodydiary.data.repository

import com.uit.melodydiary.model.GeneratedMusicDto
import com.uit.melodydiary.network.MusicApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MusicRepository(
    private val apiService: MusicApiService
) {
    suspend fun getMusic(): Flow<String> = flow {
        val musicResponse = apiService.getMusic()
        val musicList = musicResponse.value.musicList
        emit(musicResponse.value.toString())
    }

    suspend fun getGeneratedMusicByLyric(lyric: String): GeneratedMusicDto {
        return  apiService.getGeneratedMusicByLyric(lyric)
    }


}