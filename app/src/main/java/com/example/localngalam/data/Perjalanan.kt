package com.example.localngalam.data

import android.R

data class Perjalanan(
    val tanggalBerangkat: Int = 0,
    val tanggalSelesai: Int = 0,
    val tujuan: String = "",
    val kategoriTujuan: String = "",
    val idPengguna: String = "",
)
