import android.media.AudioManager
import android.media.MediaPlayer
import com.uit.melodydiary.model.MusicSmall
import java.io.IOException
import java.util.ArrayDeque


object MusicHelper {
    private const val MAX_SIZE = 10
    private var mediaPlayer: MediaPlayer? = null
    val songQueue = ArrayDeque<MusicSmall>(MAX_SIZE)
    var currentSong: MusicSmall? = null
    private var currentSongIndex = 0

    private fun initializeMediaPlayer(url: String, onPlaybackCompleted: () -> Unit) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { start() }
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
                releaseMediaPlayer()
            }
        }
    }

    fun togglePlayback(music: MusicSmall, onPlaybackCompleted: () -> Unit) {
        currentSong = music
        if (mediaPlayer == null) {
            initializeMediaPlayer(music.url, onPlaybackCompleted)
        } else {
            if (mediaPlayer?.isPlaying == true) {
                pause()
            } else {
                play(onPlaybackCompleted)
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    private fun play(onPlaybackCompleted: () -> Unit) {
        mediaPlayer?.start() ?: playNextSongInQueue(onPlaybackCompleted)
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
            currentSongIndex =
                if (currentSongIndex > 0) currentSongIndex - 1 else songQueue.size - 1
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
        if (songQueue.isEmpty()) {
            onPlaybackCompleted()
            return
        }

        fun playNext() {
            currentSongIndex = (currentSongIndex + 1) % songQueue.size
            val currentMusic = songQueue.elementAt(currentSongIndex)
            togglePlayback(currentMusic) { playNext() }
        }

        playNext()
    }

    fun playRandom(onPlaybackCompleted: () -> Unit) {
        if (songQueue.isNotEmpty()) {
            val randomMusic = songQueue.random()
            togglePlayback(randomMusic, onPlaybackCompleted)
        } else {
            onPlaybackCompleted()
        }
    }

    private fun playNextSongInQueue(onPlaybackCompleted: () -> Unit) {
        if (songQueue.isNotEmpty()) {
            val nextSong = songQueue.first()
            togglePlayback(nextSong, onPlaybackCompleted)
        } else {
            onPlaybackCompleted()
        }
    }

    fun clearAllMusic() {
//        if (songQueue.isNotEmpty()) {
//            songQueue.clear()
//        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
