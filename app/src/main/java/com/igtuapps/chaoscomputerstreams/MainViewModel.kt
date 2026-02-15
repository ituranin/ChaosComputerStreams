package com.igtuapps.chaoscomputerstreams

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igtuapps.chaoscomputerstreams.network.Conference
import com.igtuapps.chaoscomputerstreams.network.RetrofitClient
import kotlinx.coroutines.launch

sealed class ConferenceDataState {
    data class Success(val conferences: List<Conference>) : ConferenceDataState()
    data class Error(val message: String) : ConferenceDataState()
    object Loading : ConferenceDataState()
}

class MainViewModel : ViewModel() {

    private val _conferences = MutableLiveData<ConferenceDataState>()
    val conferences: LiveData<ConferenceDataState> = _conferences

    fun fetchConferences() {
        viewModelScope.launch {
            _conferences.postValue(ConferenceDataState.Loading)
            try {
                val response = RetrofitClient.instance.getConferences()
                if (response.isNotEmpty()) {
                    _conferences.postValue(ConferenceDataState.Success(response))
                } else {
                    _conferences.postValue(ConferenceDataState.Error("No streams available"))
                }
            } catch (e: Exception) {
                // Handle error
                Log.d("MainViewModel", "Error fetching conferences: ${e.message}")
                _conferences.postValue(ConferenceDataState.Error("Network error"))
            }
        }
    }
}