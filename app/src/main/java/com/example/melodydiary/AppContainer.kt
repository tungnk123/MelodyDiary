package com.example.melodydiary

import android.content.Context
import com.example.melodydiary.data.DiaryRepository
import com.example.melodydiary.data.DiaryRoomDatabase
import com.example.melodydiary.data.LocalDiaryDataSource
import com.example.melodydiary.data.RemoteDiaryDataSource
import com.example.melodydiary.network.DiaryApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

class AppContainer(private val context: Context) {
    private val baseUrl = ""
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy {
        DiaryRoomDatabase.getDatabase(context, applicationScope)
    }
    val localDiaryDataSource: LocalDiaryDataSource by lazy {
        LocalDiaryDataSource(database.diaryDao())
    }

    val remoteDiaryDataSource: RemoteDiaryDataSource by lazy {
        RemoteDiaryDataSource(retrofit.create(DiaryApiService::class.java))
    }

    val diaryRepository: DiaryRepository by lazy {
        DiaryRepository(localDiaryDataSource, remoteDiaryDataSource)
    }

}