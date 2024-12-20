package com.duma.dicodingevent.ui.finishedEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duma.dicodingevent.data.response.EventResponse
import com.duma.dicodingevent.data.response.ListEventsItem
import com.duma.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class FinishedEventViewModel : ViewModel() {
    private val _finishedEvent = MutableLiveData<List<ListEventsItem>>()
    val finishedEvent: LiveData<List<ListEventsItem>> = _finishedEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    companion object {
        private const val TAG = "FinishedViewModel"
        private const val EVENTS= 0
    }

    init {
        findFinishedEvent()
    }

    private fun findFinishedEvent() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: EventResponse = ApiConfig.getApiService().getEvents(EVENTS)
                _finishedEvent.value = response.listEvents
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
                val response: EventResponse = ApiConfig.getApiService().searchEvents(active = 0, query = query)

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