package com.example.localngalam

import android.app.Application

class Aplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase removed — Supabase is accessed via Retrofit (no global initialization needed)
    }
}