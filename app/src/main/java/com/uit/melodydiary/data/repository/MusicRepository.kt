package com.uit.melodydiary.data.repository

import com.uit.melodydiary.data.DiaryDao
import com.uit.melodydiary.network.MusicApiService

class MusicRepository(
    private val apiService: MusicApiService,
    private val dao: DiaryDao,
) {

    suspend fun getGeneratedMusicList(
        emotion: String = "",
        genre: String = "",
        instrument: String = "",
    ) = apiService.getGeneratedMusicVer2(
        emotion = emotion,
        genre = genre,
        instrument = instrument
    )

    suspend fun generateMusic(lyric: String) = apiService.getGeneratedMusicByLyric(lyric)

    fun getAllLocalSmallMusicsByEmotion(emotion: String) =
        dao.getAllMusicByEmotion(emotion = emotion)

    fun getAllLocalSmallMusicsByGroupId(groupId: String) =
        dao.getAllMusicByGroupId(groupId = groupId)


    fun getAllLocalMusic() = dao.getAllMusic()
}