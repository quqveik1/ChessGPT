package com.kurlic.chessgpt.localgames

import androidx.lifecycle.LiveData

class LocalGameRepository(private val gameDao: GameDao) {

    val allGames: LiveData<List<LocalGame>> = gameDao.getAll()
}