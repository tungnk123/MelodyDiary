import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.uit.melodydiary.model.MusicSmall
import java.util.ArrayDeque

object MusicHelper {
    private const val TAG = "MusicHelper"
    private const val MAX_SIZE = 10
    private var exoPlayer: ExoPlayer? = null
    val songQueue = ArrayDeque<MusicSmall>(MAX_SIZE)
    var currentSong: MusicSmall? = null
    private var currentSongIndex = 0

    fun initializeExoPlayer(context: Context) {
        exoPlayer = ExoPlayer.Builder(context)
            .build()
            .apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            next {}
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        Log.e(
                            TAG,
                            "ExoPlayer error: ${error.message}"
                        )
                        releaseExoPlayer()
                    }
                })
            }
    }

    fun togglePlayback(music: MusicSmall) {
        currentSong = music
        Log.d(
            TAG,
            "Current song queue: $songQueue"
        )
        Log.d(
            TAG,
            "Current song: $music"
        )
        setNewMusicSource(music.url)
    }

    private fun setNewMusicSource(url: String) {
        exoPlayer?.apply {
            stop()
            clearMediaItems()
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            Log.d(
                TAG,
                "ExoPlayer is prepared, starting playback"
            )
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun resume() {
        exoPlayer?.play()
    }

    fun next(onPlaybackCompleted: () -> Unit) {
        if (songQueue.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songQueue.size
            val nextSong = songQueue.elementAt(currentSongIndex)
            togglePlayback(nextSong)
        }
        else {
            onPlaybackCompleted()
        }
    }

    fun previous(onPlaybackCompleted: () -> Unit) {
        if (songQueue.isNotEmpty()) {
            currentSongIndex =
                if (currentSongIndex > 0) currentSongIndex - 1 else songQueue.size - 1
            val previousSong = songQueue.elementAt(currentSongIndex)
            togglePlayback(previousSong)
        }
        else {
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
        if (!songQueue.contains(music)) {
            songQueue.addLast(music)
            maintainQueueSize()
        }
    }

    fun playSequential() {
        if (songQueue.isEmpty()) return
        for (i in songQueue.indices) {
            currentSongIndex = (currentSongIndex + 1) % songQueue.size
            val currentMusic = songQueue.elementAt(currentSongIndex)
            togglePlayback(currentMusic)
        }
    }

    fun playRandom(onPlaybackCompleted: () -> Unit) {
        if (songQueue.isNotEmpty()) {
            val randomMusic = songQueue.random()
            togglePlayback(randomMusic)
        }
        else {
            onPlaybackCompleted()
        }
    }

    fun clearAllMusic() {
        songQueue.clear()
    }

    fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }
}
