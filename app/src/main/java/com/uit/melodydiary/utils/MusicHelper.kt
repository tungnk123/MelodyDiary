import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.uit.melodydiary.model.MusicSmall
import java.io.IOException
import java.util.ArrayDeque

object MusicHelper {
    private var mediaPlayer: MediaPlayer? = null
    private val songQueue = ArrayDeque<MusicSmall>(10)
    var currentsong = MusicSmall(
        musicId = 1,
        title = "Giai điêệu 1",
        isPlay = true
    )
    private var currentSongIndex = 0
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
                    true
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun togglePlayback(currentMusic: MusicSmall, onPlaybackCompleted: () -> Unit) {
        if (mediaPlayer == null) {
            initializeMediaPlayer(currentMusic.url, onPlaybackCompleted)
        } else {
            if (mediaPlayer?.isPlaying == true) {
                pause()
            } else {
                play(onPlaybackCompleted)
            }
        }
        currentsong = currentMusic
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun play(onPlaybackCompleted: () -> Unit) {
        mediaPlayer?.start() ?: run {
            if (songQueue.isNotEmpty()) {
                val nextSong = songQueue.first()
                togglePlayback(nextSong, onPlaybackCompleted)
            } else {
                onPlaybackCompleted()
            }
        }
    }

    fun next(onPlaybackCompleted: () -> Unit) {
        if (songQueue.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songQueue.size
            val nextSong = songQueue.elementAt(currentSongIndex)
            togglePlayback(nextSong, onPlaybackCompleted)
        } else {
            onPlaybackCompleted()
        }
    }

    fun previous(onPlaybackCompleted: () -> Unit) {
        if (songQueue.isNotEmpty()) {
            currentSongIndex = if (currentSongIndex > 0) {
                currentSongIndex - 1
            } else {
                songQueue.size - 1
            }
            val previousSong = songQueue.elementAt(currentSongIndex)
            togglePlayback(previousSong, onPlaybackCompleted)
        } else {
            onPlaybackCompleted()
        }
    }

    private fun maintainQueueSize() {
        while (songQueue.size > MAX_SIZE) {
            songQueue.removeLast()
        }
    }

    fun addSongToFront(music: MusicSmall) {
        songQueue.addFirst(music)
        maintainQueueSize()
    }

    fun addSongToEnd(music: MusicSmall) {
        songQueue.addLast(music)
        maintainQueueSize()
    }

    fun playSequential(onPlaybackCompleted: () -> Unit) {
        fun playNext() {
            if (songQueue.isNotEmpty()) {
                currentSongIndex = (currentSongIndex + 1) % songQueue.size
                val currentMusic = songQueue.elementAt(currentSongIndex)
                togglePlayback(currentMusic) {
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
            val randomMusic = songQueue.random()
            togglePlayback(randomMusic) {
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

