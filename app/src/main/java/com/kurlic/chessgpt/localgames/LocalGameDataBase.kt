package com.kurlic.chessgpt.localgames

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocalGame::class], version = 4)
abstract class LocalGameDataBase : RoomDatabase()
{
    abstract fun localGameDao(): LocalGameDao
    companion object {
        @Volatile
        private var INSTANCE: LocalGameDataBase? = null
        private val DBNAME = "local_game_database"

        fun getDatabase(context: Context): LocalGameDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalGameDataBase::class.java,
                    DBNAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}