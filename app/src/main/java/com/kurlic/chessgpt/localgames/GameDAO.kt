package com.kurlic.chessgpt.localgames

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM localgame")
    fun getAll(): LiveData<List<LocalGame>>

    @Insert
    suspend fun insert(game: LocalGame)
}
