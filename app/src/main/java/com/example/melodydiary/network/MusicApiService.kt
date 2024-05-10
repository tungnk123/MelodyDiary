

package com.example.melodydiary.network

import com.example.melodydiary.model.GeneratedMusicDto
import com.example.melodydiary.model.MusicResponse
import com.example.melodydiary.model.MusicSmall
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("Musics")
    suspend fun getMusic(): MusicResponse

    @GET("/api/v1/generated-musics/by-lyric")
    suspend fun getGeneratedMusicByLyric(
        @Query("lyric") lyric: String
    ): GeneratedMusicDto
}