package com.example.localngalam.data.remote

import com.example.localngalam.data.remote.dto.AuthResponse
import com.example.localngalam.data.remote.dto.BucketListDto
import com.example.localngalam.data.remote.dto.CreateBucketListRequest
import com.example.localngalam.data.remote.dto.CreateJourneyRequest
import com.example.localngalam.data.remote.dto.CreateReviewRequest
import com.example.localngalam.data.remote.dto.GoogleSignInRequest
import com.example.localngalam.data.remote.dto.JourneyDto
import com.example.localngalam.data.remote.dto.LoginRequest
import com.example.localngalam.data.remote.dto.RegisterRequest
import com.example.localngalam.data.remote.dto.ResetPasswordRequest
import com.example.localngalam.data.remote.dto.ReviewDto
import com.example.localngalam.data.remote.dto.TempatDto
import com.example.localngalam.data.remote.dto.UpdateDaftarPerjalananRequest
import com.example.localngalam.data.remote.dto.UpdateReviewRequest
import com.example.localngalam.data.remote.dto.UpdateTipePerjalananRequest
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

    // STORAGE

    @POST("storage/v1/object/avatars/{filename}")
    suspend fun uploadAvatar(
        @retrofit2.http.Path("filename") filename: String,
        @Body file: okhttp3.RequestBody
    ): Response<Unit>

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

    @GET("rest/v1/journey")
    suspend fun getJourneyById(
        @Query("id") id: String,
        @Query("select") select: String = "*"
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

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/journey")
    suspend fun updateJourneyType(
        @Query("id") journeyId: String,
        @Body update: UpdateTipePerjalananRequest
    ): Response<List<JourneyDto>>

    @DELETE("rest/v1/journey")
    suspend fun deleteJourney(@Query("id") journeyId: String): Response<Unit>

    // REVIEWS

    @GET("rest/v1/reviews")
    suspend fun getReviews(
        @Query("destination_id") destinationId: String,
        @Query("select") select: String,
        @Query("order") order: String = "created_at.desc"
    ): Response<List<ReviewDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/reviews")
    suspend fun addReview(@Body review: CreateReviewRequest): Response<List<ReviewDto>>

    @PATCH("rest/v1/reviews")
    suspend fun updateReview(
        @Query("id") reviewId: String,
        @Body update: UpdateReviewRequest
    ): Response<Unit>

    @DELETE("rest/v1/reviews")
    suspend fun deleteReview(@Query("id") reviewId: String): Response<Unit>

    // BUCKET LIST

    @GET("rest/v1/bucket_list")
    suspend fun getBucketList(
        @Query("user_id") userId: String,
        @Query("select") select: String
    ): Response<List<BucketListDto>>

    @GET("rest/v1/bucket_list")
    suspend fun getBucketListByDestination(
        @Query("user_id") userId: String,
        @Query("destination_id") destinationId: String,
        @Query("select") select: String
    ): Response<List<BucketListDto>>

    @Headers("Prefer: resolution=ignore-duplicates")
    @POST("rest/v1/bucket_list")
    suspend fun addToBucketList(@Body request: CreateBucketListRequest): Response<Unit>

    @DELETE("rest/v1/bucket_list")
    suspend fun deleteFromBucketList(
        @Query("user_id") userId: String,
        @Query("destination_id") destinationId: String
    ): Response<Unit>
}
