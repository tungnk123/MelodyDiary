package com.uit.melodydiary.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.uit.melodydiary.MelodyDiaryApplication
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException

class MusicWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(
    appContext,
    workerParams
) {
    private val application = appContext as MelodyDiaryApplication
    private val musicRepository = application.container.musicRepository

    override suspend fun doWork(): Result = coroutineScope {
        val lyric = inputData.getString(KEY_LYRIC) ?: Result.failure()

        try {
            val response = musicRepository.generateMusic(lyric.toString())
            Log.d(
                "test_worker",
                "Response: $response"
            )
            Result.success()
        }
        catch (e: HttpException) {
            Result.retry()
        }
        catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val KEY_LYRIC = "key_lyric"
        private const val KEY_RESULT = "key_result"

        fun scheduleMusicWorker(
            workManager: WorkManager,
            lyric: String,
        ) {
            val workRequest =
                OneTimeWorkRequestBuilder<MusicWorker>().setInputData(workDataOf(KEY_LYRIC to lyric))
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()

            workManager.enqueue(workRequest)
        }
    }
}
