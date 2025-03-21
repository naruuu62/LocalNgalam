package com.example.localngalam.model

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class UserData(
    val namaLengkap: String = "",
    val noTelepon: String = "",
    val email: String = "",
    val uid: String = ""
)

fun saveUserDataFireStore(userData: com.example.localngalam.model.UserData) {
    val db = Firebase.firestore
    val userCollection = db.collection("users")
    userCollection.add(userData)



}