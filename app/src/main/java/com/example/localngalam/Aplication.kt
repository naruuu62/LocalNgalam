package com.example.localngalam

import android.app.Application
import com.google.firebase.FirebaseApp



class Aplication  : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // Inisialisasi Firebase
    }
}