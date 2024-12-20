package com.duma.dicodingevent.ui.detailEvent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duma.dicodingevent.data.response.Event
import com.duma.dicodingevent.data.response.EventDetailResponse
import com.duma.dicodingevent.data.retrofit.ApiConfig
import com.duma.dicodingevent.ui.favoriteEvent.database.AppDatabase
import com.duma.dicodingevent.ui.favoriteEvent.database.FavoriteEvent
import com.duma.dicodingevent.ui.favoriteEvent.repository.FavoriteEventRepository
import kotlinx.coroutines.launch
import java.io.IOException

class DetailEventViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteEventRepository

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _detailEvent = MutableLiveData<Event>()
    val detailEvent: LiveData<Event> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchDetailEvent(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response: EventDetailResponse = ApiConfig.getApiService().getDetailEvent(id)
                _detailEvent.value = response.event
                _errorMessage.value = ""
            } catch (e: IOException) {
                _errorMessage.value = "Network error. Please check your connection."
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load event details. Try again"
            } finally {
                _isLoading.value = false
            }
        }
    }

    init {
        val favoriteEventDao = AppDatabase.getDatabase(application).favoriteEventDao()
        repository = FavoriteEventRepository(favoriteEventDao)
    }

    fun checkFavoriteStatus(eventId: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.isFavorite(eventId)
        }
    }

    fun toggleFavorite(event: Event) {
        viewModelScope.launch {
            val favoriteEvent = FavoriteEvent(
                id = event.id,
                name = event.name,
                ownerName = event.ownerName,
                beginTime = event.beginTime,
                quota = event.quota,
                registrants = event.registrants,
                description = event.description,
                mediaCover = event.mediaCover
            )
            if (_isFavorite.value == true) {
                repository.delete(favoriteEvent)
            } else {
                repository.insert(favoriteEvent)
            }
            _isFavorite.value = !_isFavorite.value!!
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }


}