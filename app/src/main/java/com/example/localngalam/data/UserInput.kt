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
    val email: String = "",
    val uid: String = ""
)

fun saveUserDataFireStore(userData: com.example.localngalam.data.UserData) {
    val db = Firebase.firestore
    val userCollection = db.collection("users")
    userCollection.add(userData)



}