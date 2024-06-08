package com.uit.melodydiary.ui.music

import MusicHelper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uit.melodydiary.MelodyDiaryApplication
import com.uit.melodydiary.R
import com.uit.melodydiary.data.repository.AlbumRepository
import com.uit.melodydiary.data.repository.MusicRepository
import com.uit.melodydiary.model.Album
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.model.Music
import com.uit.melodydiary.model.MusicSmall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime


private const val FETCH_INTERVAL = 5000L
class MusicViewModel(
    private val musicRepository: MusicRepository,
    private val albumRepository: AlbumRepository
) : ViewModel() {

    var albumList: StateFlow<List<Album>> = MutableStateFlow(mutableListOf())
    var musicSmallList: List<MusicSmall> = listOf()
    var currentDiary: Diary = Diary(
        diaryId = 0,
        title = "Chọn",
        content = "Content",
        createdAt = LocalDateTime.now(),
        logo = R.drawable.ic_face,
        mood = "fun",
        contentList = listOf(


        )
    )
    suspend fun fetchMusic(lyric: String): String {
        try {
            val musicResponse = withContext(Dispatchers.IO) {
                musicRepository.getGeneratedMusicByLyric(lyric)
            }
            return musicResponse.value.fileContentUrl
        } catch (e: Exception) {
            Log.e("fetchMusic", "Error fetching music: ${e.message}", e)
            throw e
        }
    }

    suspend fun populateMusicList(lyric: String) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                while(isActive) {
                    val musicResponse = withContext(Dispatchers.IO) {
                        musicRepository.getGeneratedMusicByLyric(lyric)
                    }
                    MusicHelper.addSongToFront(musicResponse.value.fileContentUrl)
                    delay(FETCH_INTERVAL)
                }
            }
        } catch (e: Exception) {
            Log.e("fetchMusic", "Error fetching music: ${e.message}", e)
            throw e
        }
    }

    fun insertAlbum(album: Album) {

        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.insertAlbum(album)
        }
    }

    fun getAllAlbum() {
        viewModelScope.launch {
            albumList = albumRepository.getAlbum().stateIn(
                scope = viewModelScope,
                initialValue = listOf<Album>(),
                started = SharingStarted.WhileSubscribed(1_000)
            )
        }
    }
    fun insertMusic(music: MusicSmall) {

        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.insertMusic(music)
        }
    }
    fun getAllMusic(){
        viewModelScope.launch(Dispatchers.IO) {
            musicSmallList = albumRepository.getAllMusic()
        }
    }




    fun setSelectedDiary(diary: Diary) {
        currentDiary = diary
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MelodyDiaryApplication)
                val musicRepository = application.container.musicRepository
                val albumRepository = application.container.albumRepository
                MusicViewModel(musicRepository, albumRepository)
            }
        }
    }
}
