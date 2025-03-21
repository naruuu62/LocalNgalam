package com.example.localngalam.model

import Tempat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class  Perjalanan(
    val namaPerjalanan: String = "",
    val tanggalBerangkat: String = "",
    val tanggalSelesai: String= "",
    val idPengguna: String = "",
    val tipePerjalanan: String = "",
    val daftarPerjalanan: List<tempatPerjalanan> = emptyList(),
    val jamMulai: String = "",
    val jamSelesai: String = ""
)

