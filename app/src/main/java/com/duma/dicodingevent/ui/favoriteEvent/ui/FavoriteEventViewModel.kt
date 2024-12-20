package com.duma.dicodingevent.ui.favoriteEvent.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.duma.dicodingevent.ui.favoriteEvent.database.AppDatabase
import com.duma.dicodingevent.ui.favoriteEvent.database.FavoriteEvent
import com.duma.dicodingevent.ui.favoriteEvent.repository.FavoriteEventRepository

class FavoriteEventViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteEventRepository
    val favoriteEvents: LiveData<List<FavoriteEvent>>

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        val favoriteEventDao = AppDatabase.getDatabase(application).favoriteEventDao()
        repository = FavoriteEventRepository(favoriteEventDao)
        favoriteEvents = repository.favoriteEvents
    }
}
