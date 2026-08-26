package com.rombsquare.solocards.features.cloud_feature.data

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rombsquare.solocards.features.cloud_feature.domain.repos.AuthRepo
import kotlinx.coroutines.tasks.await

class GoogleAuthRepo(
    private val context: Context
): AuthRepo {
    private val appContext = context.applicationContext

    private val auth by lazy {
        if (FirebaseApp.getApps(appContext).isEmpty()) {
            FirebaseApp.initializeApp(appContext)
        }
        FirebaseAuth.getInstance()
    }
    private val credentialManager by lazy { CredentialManager.create(appContext) }

    init {
        if (FirebaseApp.getApps(appContext).isEmpty()) {
            FirebaseApp.initializeApp(appContext)
        }
    }

    override suspend fun signIn(webClientId: String): Result<Boolean> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(webClientId)
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is androidx.credentials.CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                auth.signInWithCredential(firebaseCredential).await()
                Result.success(true)
            } else {
                Result.failure(Exception("Invalid credential type"))
            }
        } catch (e: GetCredentialException) {
            Log.e("FirebaseTest", e.errorMessage.toString())
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("FirebaseTest", e.toString())
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()

        val clearRequest = ClearCredentialStateRequest()
        credentialManager.clearCredentialState(clearRequest)
    }
}