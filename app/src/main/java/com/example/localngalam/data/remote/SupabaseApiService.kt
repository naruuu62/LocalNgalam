package com.example.localngalam.data.remote

import com.example.localngalam.data.remote.dto.AuthResponse
import com.example.localngalam.data.remote.dto.CreateJourneyRequest
import com.example.localngalam.data.remote.dto.GoogleSignInRequest
import com.example.localngalam.data.remote.dto.JourneyDto
import com.example.localngalam.data.remote.dto.LoginRequest
import com.example.localngalam.data.remote.dto.RegisterRequest
import com.example.localngalam.data.remote.dto.ResetPasswordRequest
import com.example.localngalam.data.remote.dto.TempatDto
import com.example.localngalam.data.remote.dto.UpdateDaftarPerjalananRequest
import com.example.localngalam.data.remote.dto.UpsertUserRequest
import com.example.localngalam.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query


interface SupabaseApiService {

    // AUTH

    @POST("auth/v1/signup")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/v1/token")
    suspend fun login(
        @Query("grant_type") grantType: String,
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/v1/token")
    suspend fun signInWithGoogle(
        @Query("grant_type") grantType: String,
        @Body request: GoogleSignInRequest
    ): Response<AuthResponse>

    @POST("auth/v1/recover")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>

    // USERS

    @GET("rest/v1/users")
    suspend fun getUserById(
        @Query("uid") uid: String,
        @Query("select") select: String
    ): Response<List<UserDto>>

    @Headers("Prefer: resolution=merge-duplicates")
    @POST("rest/v1/users")
    suspend fun upsertUser(@Body user: UpsertUserRequest): Response<Unit>

    // TEMPAT

    @GET("rest/v1/tempat")
    suspend fun getTempat(
        @Query("select") select: String,
        @Query("category") category: String?
    ): Response<List<TempatDto>>

    @GET("rest/v1/tempat")
    suspend fun getTempatById(
        @Query("id") id: String,
        @Query("select") select: String
    ): Response<List<TempatDto>>

    // JOURNEY

    @GET("rest/v1/journey")
    suspend fun getJourneyByUser(
        @Query("id_pengguna") idPengguna: String,
        @Query("select") select: String,
        @Query("order") order: String,
        @Query("limit") limit: Int
    ): Response<List<JourneyDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/journey")
    suspend fun createJourney(@Body journey: CreateJourneyRequest): Response<List<JourneyDto>>

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/journey")
    suspend fun updateJourney(
        @Query("id") journeyId: String,
        @Body update: UpdateDaftarPerjalananRequest
    ): Response<List<JourneyDto>>

    @DELETE("rest/v1/journey")
    suspend fun deleteJourney(@Query("id") journeyId: String): Response<Unit>
}
