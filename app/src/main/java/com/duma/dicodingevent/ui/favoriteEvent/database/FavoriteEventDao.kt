package com.duma.dicodingevent.ui.favoriteEvent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favorite_events")
    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun delete(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM favorite_events WHERE id = :eventId LIMIT 1")
    suspend fun getFavoriteEventById(eventId: Int): FavoriteEvent?
}

