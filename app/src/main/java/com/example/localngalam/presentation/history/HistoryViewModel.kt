package com.example.localngalam.presentation.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.repository.JourneyRepository
import com.example.localngalam.model.Perjalanan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val journeyRepository = JourneyRepository(sessionManager)

    private val _journeyList = MutableStateFlow<List<Perjalanan>>(emptyList())
    val journeyList: StateFlow<List<Perjalanan>> = _journeyList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchJourneyData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val journeys = journeyRepository.getAllJourneys()
                _journeyList.value = journeys
                Log.d("HistoryViewModel", "Berhasil mengambil ${journeys.size} perjalanan")
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Gagal mengambil data perjalanan: ${e.message}", e)
            }
            _isLoading.value = false
        }
    }
}
