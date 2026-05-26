    package com.example.localngalam.data.repository

    import android.util.Log
    import com.example.localngalam.data.local.SessionManager
    import com.example.localngalam.data.remote.RetrofitClient
    import com.example.localngalam.data.remote.dto.GoogleSignInRequest
    import com.example.localngalam.data.remote.dto.LoginRequest
    import com.example.localngalam.data.remote.dto.RegisterRequest
    import com.example.localngalam.data.remote.dto.ResetPasswordRequest
    import com.example.localngalam.model.UserData
    import okhttp3.MediaType.Companion.toMediaTypeOrNull

    private const val TAG = "AuthRepository"

    class AuthRepository(private val sessionManager: SessionManager) {

        private val api = RetrofitClient.unauthenticated
        private val userRepository = UserRepository(sessionManager)

        suspend fun login(email: String, password: String): Result<UserData> {
            return try {
                val response = api.login(grantType = "password", request = LoginRequest(email, password))
                if (response.isSuccessful) {
                    val body = response.body()
                        ?: return Result.failure(Exception("Response body null"))
                    val accessToken = body.accessToken
                        ?: return Result.failure(Exception("Token kosong"))
                    val refreshToken = body.refreshToken ?: ""
                    val userId = body.user?.id
                        ?: return Result.failure(Exception("User ID kosong"))
                    val userEmail = body.user.email ?: email

                    sessionManager.saveSession(accessToken, refreshToken, userId, userEmail)

                    val userData = userRepository.getUserById(userId) ?: UserData(
                        uid = userId,
                        email = userEmail,
                        namaLengkap = body.user.userMetadata?.fullName ?: "",
                        noTelepon = ""
                    )
                    Result.success(userData)
                } else {
                    Log.e(TAG, "Login gagal: ${response.code()} ${response.errorBody()?.string()}")
                    Result.failure(Exception("Email atau password salah"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login exception: ${e.message}", e)
                Result.failure(e)
            }
        }

        suspend fun register(
            email: String,
            password: String,
            namaLengkap: String,
            noTelepon: String,
            avatarBytes: ByteArray? = null
        ): Result<UserData> {
            return try {
                val request = RegisterRequest(
                    email = email,
                    password = password,
                    data = com.example.localngalam.data.remote.dto.UserMetadata(
                        namaLengkap = namaLengkap,
                        noTelepon = noTelepon
                    )
                )
                val response = api.register(request)
                if (response.isSuccessful) {
                    val body = response.body()
                        ?: return Result.failure(Exception("Response body null"))
                    val accessToken = body.accessToken
                        ?: return Result.failure(Exception("Token kosong"))
                    val refreshToken = body.refreshToken ?: ""
                    val userId = body.user?.id
                        ?: return Result.failure(Exception("User ID kosong"))
                    val userEmail = body.user.email ?: email

                    sessionManager.saveSession(accessToken, refreshToken, userId, userEmail)

                    var avatarUrl: String? = null
                    if (avatarBytes != null) {
                        try {
                            val authenticatedApi = RetrofitClient.authenticated(accessToken)
                            val filename = "$userId-${System.currentTimeMillis()}.jpg"
                            val requestBody = okhttp3.RequestBody.create("image/jpeg".toMediaTypeOrNull(), avatarBytes)
                            val uploadResponse = authenticatedApi.uploadAvatar(filename, requestBody)
                            if (uploadResponse.isSuccessful) {
                                avatarUrl = "${com.example.localngalam.data.remote.SupabaseConfig.BASE_URL}storage/v1/object/public/avatars/$filename"
                            } else {
                                Log.e(TAG, "Gagal upload avatar: ${uploadResponse.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Exception upload avatar", e)
                        }
                    }

                    val userData = UserData(
                        uid = userId,
                        email = userEmail,
                        namaLengkap = namaLengkap,
                        noTelepon = noTelepon,
                        fotoProfil = avatarUrl
                    )
                    userRepository.upsertUser(userData)
                    Result.success(userData)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Register gagal: ${response.code()} $errorBody")
                    Result.failure(Exception("Gagal mendaftar"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Register exception: ${e.message}", e)
                Result.failure(e)
            }
        }

        suspend fun signInWithGoogle(idToken: String): Result<UserData> {
            return try {
                val request = GoogleSignInRequest(idToken = idToken)
                val response = api.signInWithGoogle(grantType = "id_token", request = request)
                if (response.isSuccessful) {
                    val body = response.body()
                        ?: return Result.failure(Exception("Response body null"))
                    val accessToken = body.accessToken
                        ?: return Result.failure(Exception("Token kosong"))
                    val refreshToken = body.refreshToken ?: ""
                    val userId = body.user?.id
                        ?: return Result.failure(Exception("User ID kosong"))
                    val userEmail = body.user.email ?: ""
                    val displayName = body.user.userMetadata?.fullName
                        ?: body.user.userMetadata?.namaLengkap ?: ""

                    sessionManager.saveSession(accessToken, refreshToken, userId, userEmail)

                    val userData = UserData(
                        uid = userId,
                        email = userEmail,
                        namaLengkap = displayName,
                        noTelepon = ""
                    )
                    userRepository.upsertUser(userData)
                    Result.success(userData)
                } else {
                    Log.e(TAG, "Google Sign-In gagal: ${response.code()} ${response.errorBody()?.string()}")
                    Result.failure(Exception("Google Sign-In gagal"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Google Sign-In exception: ${e.message}", e)
                Result.failure(e)
            }
        }

        suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
            return try {
                val response = api.resetPassword(ResetPasswordRequest(email))
                if (response.isSuccessful) {
                    Log.d(TAG, "Email reset password terkirim ke $email")
                    Result.success(Unit)
                } else {
                    Log.e(TAG, "Reset password gagal: ${response.code()}")
                    Result.failure(Exception("Gagal mengirim email reset password"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Reset password exception: ${e.message}", e)
                Result.failure(e)
            }
        }

        fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
        fun signOut() = sessionManager.clearSession()
        fun getCurrentUserId(): String? = sessionManager.getUserId()
        fun getCurrentUserEmail(): String? = sessionManager.getUserEmail()
    }
