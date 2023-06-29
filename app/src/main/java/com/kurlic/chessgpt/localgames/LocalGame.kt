package com.kurlic.chessgpt.localgames

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalGame(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val gameData: String
)
