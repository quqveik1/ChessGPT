package com.kurlic.chessgpt.localgames

import androidx.lifecycle.LiveData

class LocalGameRepository(private val gameDao: LocalGameDao) {
    val allGames: LiveData<List<LocalGame>> = gameDao.getAll()
}