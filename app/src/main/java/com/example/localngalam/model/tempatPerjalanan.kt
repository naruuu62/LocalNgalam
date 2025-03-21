package com.example.localngalam.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class tempatPerjalanan(
    val tempatId: String = "",
    val namaTempat: String = "",
    val jamMulai: String = "",
    val jamSelesai: String = "",
    val gambar: String = "",
    val address: String = "",
    val deskripsi: String = "",
    val nomorTelepon: String = "",
    val tanggal: String =  ""
    ) : Parcelable

