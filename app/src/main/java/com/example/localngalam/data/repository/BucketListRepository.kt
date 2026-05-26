package com.example.localngalam.data.repository

import android.util.Log
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.remote.RetrofitClient
import com.example.localngalam.data.remote.dto.BucketListDto
import com.example.localngalam.data.remote.dto.CreateBucketListRequest

private const val TAG = "BucketListRepository"

data class BucketListItem(
    val id: String = "",
    val userId: String = "",
    val destinationId: String = "",
    val createdAt: String = ""
)

class BucketListRepository(private val sessionManager: SessionManager) {

    private fun api() = RetrofitClient.authenticated(
        sessionManager.getAccessToken() ?: ""
    )

    suspend fun getBucketList(): List<BucketListItem> {
        val uid = sessionManager.getUserId() ?: return emptyList()
        return try {
            val response = api().getBucketList(userId = "eq.$uid", select = "*")
            if (response.isSuccessful) {
                response.body()?.map { it.toModel() } ?: emptyList()
            } else {
                Log.e(TAG, "getBucketList gagal: ${response.code()} ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getBucketList exception: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun addToBucketList(destinationId: String): Boolean {
        val uid = sessionManager.getUserId() ?: return false
        return try {
            val response = api().addToBucketList(
                CreateBucketListRequest(userId = uid, destinationId = destinationId)
            )
            if (response.isSuccessful) {
                Log.d(TAG, "Berhasil tambah ke bucket list: $destinationId")
                true
            } else {
                Log.e(TAG, "addToBucketList gagal: ${response.code()} ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "addToBucketList exception: ${e.message}", e)
            false
        }
    }

    suspend fun deleteFromBucketList(destinationId: String): Boolean {
        val uid = sessionManager.getUserId() ?: return false
        return try {
            val response = api().deleteFromBucketList(
                userId = "eq.$uid",
                destinationId = "eq.$destinationId"
            )
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "deleteFromBucketList exception: ${e.message}", e)
            false
        }
    }

    suspend fun isInBucketList(destinationId: String): Boolean {
        val list = getBucketList()
        return list.any { it.destinationId == destinationId }
    }

    private fun BucketListDto.toModel() = BucketListItem(
        id = id,
        userId = userId,
        destinationId = destinationId,
        createdAt = createdAt
    )
}
