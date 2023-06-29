package com.kurlic.chessgpt.localgames

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class LocalGameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocalGameRepository
    val allGames: LiveData<List<LocalGame>>

    init {
        val gamesDao = LocalGameDataBase.getDatabase(application).gameDao()
        repository = LocalGameRepository(gamesDao)
        allGames = repository.allGames
    }
}
