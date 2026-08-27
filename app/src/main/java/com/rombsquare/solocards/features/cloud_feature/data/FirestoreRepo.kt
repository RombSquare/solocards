package com.rombsquare.solocards.features.cloud_feature.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.rombsquare.solocards.features.menu_feature.domain.models.Progress
import com.rombsquare.solocards.features.cloud_feature.data.mappers.toDomain
import com.rombsquare.solocards.features.cloud_feature.data.mappers.toFirestoreObject
import com.rombsquare.solocards.features.cloud_feature.data.models.MiscDataObject
import com.rombsquare.solocards.features.cloud_feature.data.models.ProgressObject
import com.rombsquare.solocards.features.cloud_feature.domain.models.MiscData
import com.rombsquare.solocards.features.cloud_feature.domain.repos.CloudStorageRepo
import kotlinx.coroutines.tasks.await

class FirestoreRepo: CloudStorageRepo {
    val db = Firebase.firestore
    val auth = Firebase.auth

    override fun exportData(progress: Progress) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Log.e("FirebaseTest", "User isn't logged in!")
            return
        }

        // Export progress
        db.collection("users")
            .document(userId)
            .collection("data")
            .document("current_data")
            .set(progress.toFirestoreObject())
            .addOnSuccessListener { Log.d("FirebaseTest", "Exported successfully") }
            .addOnFailureListener { Log.e("FirebaseTest", "Error: $it") }

        // Export misc data (modified at and quiz count)
        db.collection("users")
            .document(userId)
            .collection("data")
            .document("misc")
            .set(
                MiscDataObject(
                    modifiedAt = Timestamp.now(),
                    quizCount = progress.quizzes.size
                )
            )
            .addOnSuccessListener { Log.d("FirebaseTest", "Exported misc-data successfully") }
            .addOnFailureListener { Log.e("FirebaseTest", "Error during exporting misc-data: $it") }
    }

    override fun importData(
        onResult: (Progress?) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Log.e("FirebaseTest", "User isn't logged in!")
            return
        }

        db.collection("users")
            .document(userId)
            .collection("data")
            .document("current_data")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val progress = documentSnapshot
                        .toObject<ProgressObject>()
                        ?.toDomain()

                    onResult(progress)
                } else {
                    onResult(null)
                }
            }.addOnFailureListener { exception ->
                Log.e("FirebaseTest", "Cannot import data: ${exception.message}")
                onResult(null)
            }
    }

    override suspend fun getMisc(onResult: (MiscData?) -> Unit) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Log.e("FirebaseTest", "User isn't logged in!")
            return
        }

        db.collection("users")
            .document(userId)
            .collection("data")
            .document("misc")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val miscData = documentSnapshot
                        .toObject<MiscDataObject>()
                        ?.toDomain()

                    onResult(miscData)
                } else {
                    onResult(null)
                }
            }.addOnFailureListener { exception ->
                Log.e("FirebaseTest", "Cannot import misc-data: ${exception.message}")
                onResult(null)
            }
            .await()
    }
}