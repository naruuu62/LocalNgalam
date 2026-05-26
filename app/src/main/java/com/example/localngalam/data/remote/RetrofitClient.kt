package com.example.localngalam.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton Retrofit client configured for Supabase REST API.
 *
 * Two instances are provided:
 * - [unauthenticated]: for auth endpoints (login, register, reset) that don't need Bearer token
 * - [authenticated]: for data endpoints requiring Authorization: Bearer <access_token>
 */
object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /** Base OkHttpClient with anon key header (required by every Supabase request) */
    private fun baseOkHttpClient(accessToken: String? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .addHeader("apikey", SupabaseConfig.ANON_KEY)
                    .addHeader("Content-Type", "application/json")

                // If an access token is supplied, add Authorization header
                if (!accessToken.isNullOrBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $accessToken")
                } else {
                    // Unauthenticated: use anon key as Bearer for auth endpoints
                    requestBuilder.addHeader("Authorization", "Bearer ${SupabaseConfig.ANON_KEY}")
                }

                chain.proceed(requestBuilder.build())
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /** Retrofit instance for Auth endpoints (no user token needed) */
    val unauthenticated: SupabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SupabaseConfig.BASE_URL)
            .client(baseOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApiService::class.java)
    }

    /**
     * Returns an authenticated Retrofit service with the user's JWT.
     * Call this after login — pass the access_token from SessionManager.
     */
    fun authenticated(accessToken: String): SupabaseApiService {
        return Retrofit.Builder()
            .baseUrl(SupabaseConfig.BASE_URL)
            .client(baseOkHttpClient(accessToken))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApiService::class.java)
    }
}
