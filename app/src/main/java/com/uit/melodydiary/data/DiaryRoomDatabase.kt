package com.uit.melodydiary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uit.melodydiary.model.Album
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.model.MusicSmall
import com.uit.melodydiary.utils.Converters
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Diary::class, Album::class, MusicSmall::class], version = 1)
@TypeConverters(Converters::class)
abstract class DiaryRoomDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: DiaryRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): DiaryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryRoomDatabase::class.java,
                    "diary_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DiaryDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DiaryDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL("PRAGMA page_size = 65536;")
            db.execSQL("PRAGMA cache_size = 5000;")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            db.execSQL("PRAGMA cache_size = 5000;")
        }
    }
}
