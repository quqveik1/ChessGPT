package com.kurlic.chessgpt.localgames

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class LocalGameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocalGameRepository
    val allGames: LiveData<List<LocalGame>>
    val localGameDao: LocalGameDao

    init {
        localGameDao = LocalGameDataBase.getDatabase(application).localGameDao()
        repository = LocalGameRepository(localGameDao)
        allGames = repository.allGames
    }
}
