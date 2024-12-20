package com.duma.dicodingevent.ui.upcomingEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duma.dicodingevent.data.response.ListEventsItem
import com.duma.dicodingevent.data.response.EventResponse
import com.duma.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class UpcomingEventViewModel : ViewModel() {
    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    companion object {
        private const val TAG = "UpcomingViewModel"
        private const val EVENTS= 1
    }

    init {
        findUpcomingEvent()
    }

    private fun findUpcomingEvent() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: EventResponse = ApiConfig.getApiService().getEvents(
                    EVENTS
                )
                _upcomingEvent.value = response.listEvents
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _errorMessage.value = "Failed to load events. Please check your internet connection."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchEvents(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: EventResponse = ApiConfig.getApiService().searchEvents(active = 1, query = query)

                _events.value = response.listEvents
                _errorMessage.value = null
            } catch (e: Exception) {

                Log.e(TAG, "onFailure: ${e.message}")
                _errorMessage.value = "Failed to search events. Please check your internet connection."
                _events.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}