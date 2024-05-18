package com.example.melodydiary.ui.music

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.melodydiary.MelodyDiaryApplication
import com.example.melodydiary.data.repository.AlbumRepository
import com.example.melodydiary.data.repository.MusicRepository
import com.example.melodydiary.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class MusicViewModel(
    private val musicRepository: MusicRepository,
    private val albumRepository: AlbumRepository
) : ViewModel() {

    var albumList: StateFlow<List<Album>> = MutableStateFlow(mutableListOf())
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

    suspend fun fetchMusicList(lyric: String): List<String> {
        try {
            val musicList: MutableList<String> = mutableListOf()
            repeat(3) {
                val musicResponse = withContext(Dispatchers.IO) {
                    musicRepository.getGeneratedMusicByLyric(lyric)
                }
                musicList.add(musicResponse.value.fileContentUrl)

            }
            return musicList
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
