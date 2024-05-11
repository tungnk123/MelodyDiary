package com.example.melodydiary.data.repository

import android.util.Log
import com.example.melodydiary.model.GeneratedMusicDto
import com.example.melodydiary.model.Music
import com.example.melodydiary.model.MusicSmall
import com.example.melodydiary.network.MusicApiService
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