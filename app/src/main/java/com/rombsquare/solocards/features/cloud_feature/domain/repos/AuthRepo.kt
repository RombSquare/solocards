package com.rombsquare.solocards.features.cloud_feature.domain.repos

interface AuthRepo {
    suspend fun signIn(webClientId: String): Result<Boolean>
    suspend fun signOut()
}