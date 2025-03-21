package com.example.localngalam.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.model.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData
    val auth = FirebaseAuth.getInstance()

    init {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            getUserData(userId)
        } else {
            println("User belum login")
        }
    }

    private fun getUserData(userId: String) {
        val db = Firebase.firestore
        val docRef = db.collection("users").document(userId)
        println("Fetching data for user: $userId")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val namaLengkap = document.getString("namaLengkap") ?: "Nama tidak ditemukan"
                    println("Nama pengguna: $namaLengkap") // Debug log
                    _userData.value = UserData(
                        uid = userId,
                        namaLengkap = namaLengkap,
                        noTelepon = document.getString("noTelepon") ?: "",
                        email = document.getString("email") ?: ""
                    )
                } else {
                    println("Dokumen tidak ditemukan")
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}

