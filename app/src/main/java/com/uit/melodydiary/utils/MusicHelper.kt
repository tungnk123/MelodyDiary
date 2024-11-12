import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.uit.melodydiary.model.MusicSmall
import java.util.ArrayDeque

@UnstableApi
object MusicHelper {
    private const val TAG = "MusicHelper"
    private const val MAX_SIZE = 10
    val songQueue = ArrayDeque<MusicSmall>(MAX_SIZE)
    var currentSong: MusicSmall? = null
    private var currentSongIndex = 0
    var exoPlayer: ExoPlayer? = null
    private var simpleCache: SimpleCache? = null

    fun initializeExoPlayer(context: Context) {
        // Initialize cache for audio files
        simpleCache = SimpleCache(
            context.cacheDir,
            LeastRecentlyUsedCacheEvictor(50 * 1024 * 1024)  // 50 MB cache
        )

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                10000,
                20000,
                1000,
                3000
            )
            .build()

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache!!)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))
            .setCacheWriteDataSinkFactory(
                CacheDataSink.Factory()
                    .setCache(simpleCache!!)
            )
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        exoPlayer = ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build()
            .apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        Log.d(
                            TAG,
                            "Playback state changed: $state"
                        )
                        if (state == Player.STATE_ENDED) {
                            next {}
                        }
                        else if (state == Player.STATE_READY) {
                            Log.d(
                                TAG,
                                "ExoPlayer is ready to play."
                            )
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
        Log.d(
            TAG,
            "ExoPlayer initialized."
        )
    }

    fun togglePlayback(music: MusicSmall) {
        currentSong = music
        Log.d(
            TAG,
            "Attempting to play: ${music.title}"
        )
        setNewMusicSource(music.url)
    }

    private fun setNewMusicSource(url: String) {
        if (exoPlayer == null) {
            Log.e(
                TAG,
                "ExoPlayer is not initialized."
            )
            return
        }
        exoPlayer?.apply {
            stop()
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)

            // Prepare and ensure playback starts only when ready
            prepare()
            playWhenReady = true
            Log.d(
                TAG,
                "Media source set, preparing ExoPlayer."
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
            Log.d(
                TAG,
                "Song queue is empty."
            )
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
            Log.d(
                TAG,
                "Song queue is empty."
            )
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
        simpleCache?.release()
        simpleCache = null
        Log.d(
            TAG,
            "ExoPlayer and cache released."
        )
    }
}
