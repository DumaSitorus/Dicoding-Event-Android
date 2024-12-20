package com.duma.dicodingevent.ui.favoriteEvent.repository

import androidx.lifecycle.LiveData
import com.duma.dicodingevent.ui.favoriteEvent.database.FavoriteEvent
import com.duma.dicodingevent.ui.favoriteEvent.database.FavoriteEventDao

class FavoriteEventRepository(private val favoriteEventDao: FavoriteEventDao) {
    val favoriteEvents: LiveData<List<FavoriteEvent>> = favoriteEventDao.getFavoriteEvents()

    suspend fun insert(favoriteEvent: FavoriteEvent) {
        favoriteEventDao.insert(favoriteEvent)
    }

    suspend fun delete(favoriteEvent: FavoriteEvent) {
        favoriteEventDao.delete(favoriteEvent)
    }

    suspend fun isFavorite(eventId: Int): Boolean {
        return favoriteEventDao.getFavoriteEventById(eventId) != null
    }
}
