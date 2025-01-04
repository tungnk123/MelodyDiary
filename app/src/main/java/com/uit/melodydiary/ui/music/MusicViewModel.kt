package com.uit.melodydiary.ui.music

import MusicHelper
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.util.UnstableApi
import com.google.ai.client.generativeai.GenerativeModel
import com.uit.melodydiary.BuildConfig
import com.uit.melodydiary.MelodyDiaryApplication
import com.uit.melodydiary.R
import com.uit.melodydiary.data.repository.AlbumRepository
import com.uit.melodydiary.data.repository.MusicRepository
import com.uit.melodydiary.model.Album
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.model.MusicSmall
import com.uit.melodydiary.model.toMusicSmall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class MusicViewModel(
    private val musicRepository: MusicRepository,
    private val albumRepository: AlbumRepository,
) : ViewModel() {

    private val _albumList = MutableStateFlow<List<Album>>(emptyList())
    val albumList: StateFlow<List<Album>> get() = _albumList

    private val _musicSmallList = MutableStateFlow<List<MusicSmall>>(emptyList())
    val musicSmallList: StateFlow<List<MusicSmall>> get() = _musicSmallList

    var currentDiary: Diary = Diary(
        diaryId = 0,
        title = "Chọn",
        content = "Content",
        createdAt = LocalDateTime.now(),
        logo = R.drawable.ic_face,
        mood = "fun",
        contentFilePath = ""
    )
    val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    fun setMusicList(list: List<MusicSmall>) {
        _musicSmallList.value = list
    }

    suspend fun generateMusic(
        emotion: String = "",
        genre: String = "",
        instrument: String = "",
    ): String {
        try {
            val musicResponse = withContext(Dispatchers.IO) {
                musicRepository.generateMusic(emotion)
            }
            return musicResponse.value.fileContentUrl
        }
        catch (e: Exception) {
            Log.e(
                "fetchMusic",
                "Error fetching music: ${e.message}",
                e
            )
            throw e
        }
    }

    @OptIn(UnstableApi::class)
    fun populateMusicList(emotion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val musicList = musicRepository.getAllLocalSmallMusicsByEmotion(emotion)
                musicList.forEach { MusicHelper.addSongToEnd(it) }

                val musicGroup = musicRepository.getGeneratedMusicList(emotion)
                val remoteMusicList = musicGroup.flatMap { group ->
                    group.musics.map { it.toMusicSmall() }
                }
                remoteMusicList.forEach { MusicHelper.addSongToEnd(it) }

                Log.d(
                    "test_song",
                    "Current song queue: ${MusicHelper.songQueue}"
                )
            }
            catch (e: Exception) {
                Log.e(
                    "fetchMusic",
                    "Error fetching music: ${e.message}",
                    e
                )
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun populateMusicListByLyric(lyric: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val musicGroup = musicRepository.getGeneratedMusicListByLyric(lyric)
                val remoteMusicList = musicGroup.flatMap { group ->
                    group.musics.map { it.toMusicSmall() }
                }
                remoteMusicList.forEach { MusicHelper.addSongToEnd(it) }

                Log.d(
                    "test_song",
                    "Current song queue: ${MusicHelper.songQueue}"
                )
            }
            catch (e: Exception) {
                Log.e(
                    "fetchMusic",
                    "Error fetching music: ${e.message}",
                    e
                )
            }
        }
    }

    fun getAllMusicByGroupId(groupId: String) =
        musicRepository.getAllLocalSmallMusicsByGroupId(groupId)

    fun insertAlbum(album: Album) {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.insertAlbum(album)
        }
    }

    fun getAllAlbum() {
        viewModelScope.launch {
            albumRepository
                .getAlbum()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(1_000),
                    initialValue = emptyList()
                )
                .collect { albums ->
                    _albumList.value = albums
                }
        }
    }

    fun insertMusic(music: MusicSmall) {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.insertMusic(music)
        }
    }

    fun getAllMusic() {
        viewModelScope.launch {
            albumRepository
                .getAllMusic()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
                .collect { musicList ->
                    _musicSmallList.value = musicList
                }
        }
    }

    fun setSelectedDiary(diary: Diary) {
        currentDiary = diary
    }

    fun callGeminiToDetectEmotion(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = generativeModel.generateContent(input)
            Log.d(
                "test_gemini",
                response.text.toString()
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MelodyDiaryApplication
                val musicRepository = application.container.musicRepository
                val albumRepository = application.container.albumRepository
                MusicViewModel(
                    musicRepository,
                    albumRepository
                )
            }
        }
    }
}

