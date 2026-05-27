package com.example.localngalam.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.repository.Review
import com.example.localngalam.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val reviewRepository = ReviewRepository(sessionManager)

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _addReviewSuccess = MutableStateFlow<Boolean?>(null)
    val addReviewSuccess: StateFlow<Boolean?> = _addReviewSuccess

    fun fetchReviews(destinationId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = reviewRepository.getReviews(destinationId)
            _reviews.value = result
            _isLoading.value = false
        }
    }

    fun addReview(destinationId: String, rating: Int, comment: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = reviewRepository.addReview(destinationId, rating, comment)
            _addReviewSuccess.value = success
            if (success) {
                fetchReviews(destinationId)
                onSuccess()
            }
            _isLoading.value = false
        }
    }

    fun updateReview(reviewId: String, destinationId: String, rating: Int, comment: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = reviewRepository.updateReview(reviewId, rating, comment)
            if (success) {
                fetchReviews(destinationId)
                onSuccess()
            }
            _isLoading.value = false
        }
    }

    fun deleteReview(reviewId: String, destinationId: String) {
        viewModelScope.launch {
            val success = reviewRepository.deleteReview(reviewId)
            if (success) fetchReviews(destinationId)
        }
    }

    val currentUserId: String?
        get() = sessionManager.getUserId()

    fun averageRating(): Float {
        val list = _reviews.value
        if (list.isEmpty()) return 0f
        return list.map { it.rating }.average().toFloat()
    }
}
