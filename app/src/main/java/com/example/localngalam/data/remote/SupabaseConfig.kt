package com.example.localngalam.data.remote

import com.example.localngalam.BuildConfig

object SupabaseConfig {
    val BASE_URL: String = "${BuildConfig.SUPABASE_URL}/"
    val ANON_KEY: String = BuildConfig.SUPABASE_ANON_KEY
    const val AUTH_BASE_PATH = "auth/v1"
    const val REST_BASE_PATH = "rest/v1"
}
