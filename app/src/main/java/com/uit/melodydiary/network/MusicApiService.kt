package com.uit.melodydiary.network

import com.uit.melodydiary.model.GeneratedMusicDto
import com.uit.melodydiary.model.MusicGroup
import com.uit.melodydiary.model.MusicResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("Musics")
    suspend fun getMusic(): MusicResponse

    @GET("/api/v1/generated-musics/by-lyric/")
    suspend fun getGeneratedMusicByLyric(
        @Query("lyric") lyric: String
    ): GeneratedMusicDto

    @GET("/api/v1/Configuration/warm-up")
    suspend fun warmUp()

    @GET("/api/v2/GeneratedMusicsControllerV/generated-musics")
    suspend fun getGeneratedMusicVer2(
        @Query("emotion") emotion: String = "",
        @Query("genre") genre: String = "",
        @Query("instrument") instrument: String = "",
    ): List<MusicGroup>
}