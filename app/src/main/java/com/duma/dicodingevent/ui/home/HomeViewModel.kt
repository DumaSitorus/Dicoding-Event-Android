package com.duma.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duma.dicodingevent.data.response.EventResponse
import com.duma.dicodingevent.data.response.ListEventsItem
import com.duma.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<List<ListEventsItem>>()
    val finishEvent: LiveData<List<ListEventsItem>> = _finishedEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object {
        private const val TAG_UPCOMING = "UpcomingViewModel"
        private const val EVENTS_UPCOMING= 1
        private const val TAG_FINISH = "FinishedViewModel"
        private const val EVENTS_FINISH= 0
    }

    init {
        findUpcomingEvent()
        findFinishedEvent()
    }

    private fun findFinishedEvent() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: EventResponse = ApiConfig.getApiService().getEvents(
                    EVENTS_FINISH
                )
                _finishedEvent.value = response.listEvents
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e(TAG_FINISH, "onFailure: ${e.message}")
                _errorMessage.value = "Failed to load events. Please check your internet connection."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun findUpcomingEvent() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: EventResponse = ApiConfig.getApiService().getEvents(
                    EVENTS_UPCOMING
                )
                _upcomingEvent.value = response.listEvents
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e(TAG_UPCOMING, "onFailure: ${e.message}")
                _errorMessage.value = "Failed to load events. Please check your internet connection."
            } finally {
                _isLoading.value = false
            }
        }
    }
}