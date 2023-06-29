package com.kurlic.chessgpt.localgames

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocalGame::class], version = 2)
abstract class LocalGameDataBase : RoomDatabase() {
    abstract fun localGameDao(): LocalGameDao
    companion object {
        @Volatile
        private var INSTANCE: LocalGameDataBase? = null

        fun getDatabase(context: Context): LocalGameDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalGameDataBase::class.java,
                    "local_game_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}