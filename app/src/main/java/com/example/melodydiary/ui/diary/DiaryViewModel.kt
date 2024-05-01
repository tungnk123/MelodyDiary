package com.example.melodydiary.ui.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.melodydiary.MelodyDiaryApplication
import com.example.melodydiary.data.DiaryRepository

class DiaryViewModel(private val diaryRepository: DiaryRepository): ViewModel() {


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MelodyDiaryApplication)
                val diaryRepository = application.container.diaryRepository
                DiaryViewModel(diaryRepository = diaryRepository)
            }
        }
    }
}