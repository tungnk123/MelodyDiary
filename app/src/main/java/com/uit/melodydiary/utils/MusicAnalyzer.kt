package com.uit.melodydiary.utils

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import com.uit.melodydiary.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AudioAnalyzer {
    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    suspend fun analyzeAudio(activity: MainActivity): List<Float>? = withContext(Dispatchers.IO) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
            return@withContext null
        }

        val bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            bufferSize
        )

        audioRecord.startRecording()
        val audioData = ShortArray(bufferSize)
        val pitchValues = mutableListOf<Float>()

        while (true) {
            val readSize = audioRecord.read(audioData, 0, bufferSize)

            val detectedPitch = detectPitch(audioData, readSize)
            if (detectedPitch != -1f) {
                pitchValues.add(detectedPitch)
            }
        }

        audioRecord.stop()
        audioRecord.release()
        pitchValues
    }

    private fun detectPitch(audioData: ShortArray, readSize: Int): Float {
        val autocorrelation = FloatArray(readSize)

        for (i in 0 until readSize) {
            for (j in 0 until readSize - i) {
                autocorrelation[i] += (audioData[j] * audioData[j + i]).toFloat()
            }
        }

        var maxIndex = 0
        for (i in 1 until autocorrelation.size) {
            if (autocorrelation[i] > autocorrelation[maxIndex]) {
                maxIndex = i
            }
        }

        val frequency = SAMPLE_RATE / maxIndex.toFloat()
        return if (maxIndex > 0) frequency else -1f // Return -1 if no pitch detected
    }
}
