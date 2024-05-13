package com.example.melodydiary.utils

import android.media.MediaPlayer

private var mediaPlayer: MediaPlayer? = null

fun togglePlayback(url: String, onPlaybackCompleted: () -> Unit) {
    if (mediaPlayer == null) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener { mp ->
            mp.start()
        }
        mediaPlayer?.setOnCompletionListener {
            onPlaybackCompleted()
        }
    } else {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
    }
}
