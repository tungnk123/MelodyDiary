package com.example.melodydiary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.melodydiary.model.Diary
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Diary::class], version = 1)
abstract class DiaryRoomDatabase: RoomDatabase(){
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: DiaryRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ) : DiaryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryRoomDatabase::class.java,
                    "dairy_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
//                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}