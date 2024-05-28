package com.example.melodydiary.ui.diary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.melodydiary.MelodyDiaryApplication
import com.example.melodydiary.data.repository.DiaryRepository
import com.example.melodydiary.model.Diary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



class DiaryViewModel(private val diaryRepository: DiaryRepository): ViewModel() {

    var diaryList: StateFlow<List<Diary>> = MutableStateFlow(mutableListOf())
    var diaryListAtDate: List<Diary> = mutableListOf()

    fun getDiaryFromDatabase(){
        viewModelScope.launch {
            diaryList =  diaryRepository.getDiary().stateIn(
                scope = viewModelScope,
                initialValue = mutableListOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )
        }
    }
    fun getDiaryList(): List<Diary> {
        getDiaryFromDatabase()
        return diaryList.value
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDiaryAtDateFromDatabase(date: String): List<Diary> {
        viewModelScope.launch {
            diaryRepository.getDiaryAtDate(date).collect {
                diaryListAtDate = it
            }
        }
        return diaryListAtDate
    }


    fun addDiary(diary: Diary) {
        viewModelScope.launch {
            diaryRepository.addDiary(diary)
        }
    }

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
