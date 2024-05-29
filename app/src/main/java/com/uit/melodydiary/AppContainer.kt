package com.uit.melodydiary

import android.content.Context
import com.uit.melodydiary.data.repository.DiaryRepository
import com.uit.melodydiary.data.DiaryRoomDatabase
import com.uit.melodydiary.data.LocalDiaryDataSource
import com.uit.melodydiary.data.repository.MusicRepository
import com.uit.melodydiary.data.RemoteDiaryDataSource
import com.uit.melodydiary.data.repository.AlbumRepository
import com.uit.melodydiary.network.DiaryApiService
import com.uit.melodydiary.network.MusicApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val context: Context) {
//    val authToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ilg1ZVhrNHh5b2pORnVtMWtsMll0djhkbE5QNC1jNTdkTzZRR1RWQndhTmsiLCJ0eXAiOiJKV1QifQ.eyJnaXZlbl9uYW1lIjoiVMO5bmciLCJmYW1pbHlfbmFtZSI6IsSQb8OgbiIsIm5hbWUiOiJUw7luZyDEkG_DoG4iLCJpZHAiOiJnb29nbGUuY29tIiwib2lkIjoiZDViMDYyMWItYzI3OC00MDFmLWIwMGYtOGU4ODhkZDY0NTY3Iiwic3ViIjoiZDViMDYyMWItYzI3OC00MDFmLWIwMGYtOGU4ODhkZDY0NTY3IiwiZW1haWxzIjpbImRvYW50aGFuaHR1bmduazEyM0BnbWFpbC5jb20iXSwidGZwIjoiQjJDXzFfc2lnbnVwc2lnbmluMSIsInNjcCI6IkFwaS5SZWFkQW5kV3JpdGUiLCJhenAiOiIzYTQ3YTBkMS0yNTJmLTRkY2MtYWJhZC0wNjIzZjBmOTgzZDMiLCJ2ZXIiOiIxLjAiLCJpYXQiOjE3MTUyMjQ2MzUsImF1ZCI6ImJjYjU3ZmE2LTQ1ODktNDgwZC04OTg5LWI4NzNhY2M4YTAyYiIsImV4cCI6MTcxNTIyODIzNSwiaXNzIjoiaHR0cHM6Ly91bnNpbGVudC5iMmNsb2dpbi5jb20vZTMwYTM4ZTgtNTM5OS00ZTdlLThjOWEtOWQyNWMwMjRlMjc5L3YyLjAvIiwibmJmIjoxNzE1MjI0NjM1fQ.bv0Xn-01j6dBu4NxmZENtmOJGsht01dU0Tzjd1Oe6iujBiloyLSY3pSQY5MtLr7icpW9V_PPgSHTKB55NZF1kOmitGgfPxGaZR9qnJfnBUdjI71E5qpqjd_Tc8NsbpEzxv3FQI_5G4gawohtW36mslq15Zciiit8M9NXNivFbxWbMm51aw6uy4DI37Ab4guV3JypEese55lGPu9z5zcxZFaoz-RMKTSf58Eq9HrLKNL2HbvQ76vf_XXCBYalgucKAffOhaAtn2rgvjevZXcC5-5aWzQRNY6M1LapGymjn4G-lyTzneb6bIudKarfavcFVN2sx7rOYj_Yx3ApfvQT7w"
    val okHttpClient = OkHttpClient.Builder()
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