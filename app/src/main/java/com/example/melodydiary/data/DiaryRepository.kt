package com.example.melodydiary.data

class DiaryRepository(
    private val localDiaryDataSource: LocalDiaryDataSource,
    private val remoteDiaryDataSource: RemoteDiaryDataSource,
) {

}