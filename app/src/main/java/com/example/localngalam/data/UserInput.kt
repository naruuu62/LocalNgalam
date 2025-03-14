package com.example.localngalam.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.UserData
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserData(
    val namaLengkap: String = "",
    val noTelepon: String = "",
    val email: String = ""
)

fun saveUserDataFireStore(uid: String, userData: com.example.localngalam.data.UserData) {
    val db = Firebase.firestore
    val coroutineScope = MainScope()

    coroutineScope.launch {
        try {
            db.collection("users").document(uid).set(userData).await()
            Log.d("Firestore", "Data berhasil disimpan untuk UID: $uid")
        } catch (e: Exception) {
            Log.e("Firestore", "Gagal menyimpan data: ${e.message}")
        }
    }
}