package com.example.localngalam.presentation.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HistoryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _journeyData = MutableStateFlow<Map<String, Any>?>(null)
    val journeyData: StateFlow<Map<String, Any>?> = _journeyData

    fun fetchJourneyData() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val snapshot = db.collection("journey").document(uid).get().await()
                _journeyData.value = snapshot.data
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Gagal mengambil data perjalanan", e)
            }
        }
    }
}
