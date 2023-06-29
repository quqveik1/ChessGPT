package com.kurlic.chessgpt.localgames

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocalGameViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocalGameViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
