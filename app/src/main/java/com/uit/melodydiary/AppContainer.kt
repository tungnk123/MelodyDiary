package com.uit.melodydiary

import android.content.Context
import com.uit.melodydiary.data.DiaryRoomDatabase
import com.uit.melodydiary.data.LocalDiaryDataSource
import com.uit.melodydiary.data.RemoteDiaryDataSource
import com.uit.melodydiary.data.repository.AlbumRepository
import com.uit.melodydiary.data.repository.DiaryRepository
import com.uit.melodydiary.data.repository.MusicRepository
import com.uit.melodydiary.network.DiaryApiService
import com.uit.melodydiary.network.MusicApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(private val context: Context) {
    private val timeout: Long = 5
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(timeout, TimeUnit.MINUTES)
        .readTimeout(timeout, TimeUnit.MINUTES)
        .writeTimeout(timeout, TimeUnit.MINUTES)
        .build()
    private val baseUrl = "https://un-silent-backend-mobile.azurewebsites.net"
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()
    val warmUpApiService = retrofit.create(MusicApiService::class.java)
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

    val musicRepository: MusicRepository by lazy {
        MusicRepository(retrofit.create(MusicApiService::class.java))
    }

    val albumRepository: AlbumRepository by lazy {
        AlbumRepository(diaryDao = database.diaryDao())
    }

}