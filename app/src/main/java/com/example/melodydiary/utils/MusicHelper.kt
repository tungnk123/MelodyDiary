import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import java.io.IOException

object MusicHelper {
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private fun initializeMediaPlayer(url: String, onPlaybackCompleted: () -> Unit) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { mp ->
                    mp.start()
//                    mp.isLooping = true
                }
                setOnCompletionListener {
                    releaseMediaPlayer()
                    onPlaybackCompleted()
                }
                setOnErrorListener { _, _, _ ->
                    releaseMediaPlayer()
                    // Handle error appropriately
                    true
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle exception appropriately
            }
        }
    }

    fun togglePlayback(url: String, onPlaybackCompleted: () -> Unit) {
        if (mediaPlayer == null) {
            initializeMediaPlayer(url, onPlaybackCompleted)
        } else {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            } else {
                try {
                    mediaPlayer?.apply {
                        reset()
                        setDataSource(url)
                        prepareAsync()
                        setOnPreparedListener { mp ->
                            mp.start()
//                            mp.isLooping = true
                        }
                        setOnCompletionListener {
                            releaseMediaPlayer()
                            onPlaybackCompleted()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    // Handle exception appropriately
                }
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun playSequential(urls: List<String>, onPlaybackCompleted: () -> Unit) {
        fun playNext(index: Int) {
            if (index < urls.size) {
                togglePlayback(urls[index]) {
                    playNext(index + 1)
                }
            } else {
                playNext(0)
                onPlaybackCompleted()
            }
        }

        if (urls.isNotEmpty()) {
            playNext(0)
        } else {
            onPlaybackCompleted()
        }
    }

    fun playRandom(urls: List<String>, onPlaybackCompleted: () -> Unit) {
        if (urls.isNotEmpty()) {
            val randomUrl = urls.random()
            togglePlayback(randomUrl) {
                onPlaybackCompleted()
            }
        } else {
            onPlaybackCompleted()
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
