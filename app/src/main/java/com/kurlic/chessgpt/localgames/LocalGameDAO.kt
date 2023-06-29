package com.kurlic.chessgpt.localgames

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LocalGameDao {
    @Query("SELECT * FROM localgame")
    fun getAll(): LiveData<List<LocalGame>>

    @Query("SELECT * FROM localgame WHERE id = :id")
    fun getById(id: Int): LiveData<LocalGame>

    @Insert
    suspend fun insert(game: LocalGame) : Long

    @Update
    suspend fun update(game: LocalGame)
}
