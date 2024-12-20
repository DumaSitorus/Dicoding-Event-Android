package com.duma.dicodingevent.ui.favoriteEvent.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey val id: Int,
    val name: String,
    val ownerName: String,
    val beginTime: String,
    val quota: Int,
    val registrants: Int,
    val description: String,
    val mediaCover: String
) : Parcelable