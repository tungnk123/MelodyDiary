import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.util.ArrayDeque

object MusicHelper {
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val songQueue = ArrayDeque<String>(10)
    private const val MAX_SIZE = 10

    private fun initializeMediaPlayer(url: String, onPlaybackCompleted: () -> Unit) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { mp ->
                    mp.start()
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

    private fun maintainQueueSize() {
        while (songQueue.size > MAX_SIZE) {
            songQueue.removeLast()
        }
    }

    fun addSongToFront(url: String) {
        songQueue.addFirst(url)
        maintainQueueSize()
    }

    fun addSongToEnd(url: String) {
        songQueue.addLast(url)
        maintainQueueSize()
    }

    fun playSequential(onPlaybackCompleted: () -> Unit) {
        fun playNext() {
            if (songQueue.isNotEmpty()) {
                val url = songQueue.removeFirst()
                songQueue.addLast(url)
                togglePlayback(url) {
                    playNext()
                }
            } else {
                onPlaybackCompleted()
            }
        }

        if (songQueue.isNotEmpty()) {
            playNext()
        } else {
            onPlaybackCompleted()
        }
    }

    fun playRandom(onPlaybackCompleted: () -> Unit) {
        if (songQueue.isNotEmpty()) {
            val randomUrl = songQueue.random()
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
