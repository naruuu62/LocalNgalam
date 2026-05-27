package com.example.localngalam.data.repository

import android.util.Log
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.remote.RetrofitClient
import com.example.localngalam.data.remote.dto.CreateReviewRequest
import com.example.localngalam.data.remote.dto.ReviewDto
import com.example.localngalam.data.remote.dto.UpdateReviewRequest

private const val TAG = "ReviewRepository"

data class Review(
    val id: String = "",
    val destinationId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val createdAt: String = ""
)

class ReviewRepository(private val sessionManager: SessionManager) {

    private fun api() = RetrofitClient.authenticated(
        sessionManager.getAccessToken() ?: ""
    )

    suspend fun getReviews(destinationId: String): List<Review> {
        return try {
            val response = api().getReviews(
                destinationId = "eq.$destinationId",
                select = "*",
                order = "created_at.desc"
            )
            if (response.isSuccessful) {
                response.body()?.map { it.toModel() } ?: emptyList()
            } else {
                Log.e(TAG, "getReviews gagal: ${response.code()} ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getReviews exception: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun addReview(
        destinationId: String,
        rating: Int,
        comment: String
    ): Boolean {
        val uid = sessionManager.getUserId() ?: return false
        val userName = sessionManager.getUserName() ?: "Pengguna"
        return try {
            val request = CreateReviewRequest(
                destinationId = destinationId,
                userId = uid,
                userName = userName,
                rating = rating,
                comment = comment
            )
            val response = api().addReview(request)
            if (response.isSuccessful) {
                Log.d(TAG, "Ulasan berhasil ditambahkan")
                true
            } else {
                Log.e(TAG, "addReview gagal: ${response.code()} ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "addReview exception: ${e.message}", e)
            false
        }
    }

    suspend fun updateReview(reviewId: String, rating: Int, comment: String): Boolean {
        return try {
            val request = UpdateReviewRequest(rating = rating, comment = comment)
            val response = api().updateReview(reviewId = "eq.$reviewId", update = request)
            if (response.isSuccessful) {
                Log.d(TAG, "Ulasan berhasil diupdate: $reviewId")
                true
            } else {
                Log.e(TAG, "updateReview gagal: ${response.code()} ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateReview exception: ${e.message}", e)
            false
        }
    }

    suspend fun deleteReview(reviewId: String): Boolean {
        return try {
            val response = api().deleteReview("eq.$reviewId")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "deleteReview exception: ${e.message}", e)
            false
        }
    }

    private fun ReviewDto.toModel() = Review(
        id = id,
        destinationId = destinationId,
        userId = userId,
        userName = userName,
        rating = rating,
        comment = comment,
        createdAt = createdAt
    )
}
