package com.rombsquare.solocards.features.cloud_feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rombsquare.solocards.features.cloud_feature.domain.repos.AuthRepo
import com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases.StorageUseCases
import com.rombsquare.solocards.features.cloud_feature.ui.models.ToastMessage
import com.rombsquare.solocards.features.cloud_feature.ui.models.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class CloudViewModel(
    val authClient: AuthRepo,
    val storageUseCases: StorageUseCases
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    // UiState
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    // For sending toasts
    private val _toastEffect = Channel<ToastMessage>(Channel.BUFFERED)
    val toastEffect = _toastEffect.receiveAsFlow()

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _uiState.update {
            it.copy(
                user = firebaseAuth.currentUser
            )
        }
    }

    val user = FirebaseAuth.getInstance().currentUser
    var lastActionAt: Instant = Instant.DISTANT_PAST

    init {
        //updateUser()
        auth.addAuthStateListener(authListener)
        user?.let {
            viewModelScope.launch {
                updateMiscData()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
    }

    suspend fun onNonEarlyAction(
        ifNotEarly: suspend () -> Unit
    ) {
        if (Clock.System.now() - lastActionAt > 3.seconds) {
            lastActionAt = Clock.System.now()
            ifNotEarly()
        } else {
            viewModelScope.launch {
                _toastEffect.send(ToastMessage.WaitFewSeconds)
            }

        }
    }

    suspend fun updateMiscData() {
        storageUseCases.getMisc { miscData ->
            if (miscData != null) {
                _uiState.value = _uiState.value.copy(
                    dateModified = miscData.modifiedAt,
                    quizCount = miscData.quizCount
                )
            }
        }
    }

    fun onSignIn(webClientId: String) {
        viewModelScope.launch {
            onNonEarlyAction {
                val result = authClient.signIn(webClientId)

                result.onSuccess {
                    _toastEffect.send(ToastMessage.SignedIn)
                    //delay(1000.milliseconds)
                    //updateUser()
                    updateMiscData()
                }.onFailure {
                    _toastEffect.send(ToastMessage.CannotSignIn)
                }
            }
        }
    }

    fun onSignOut() {
        viewModelScope.launch {
            onNonEarlyAction {
                try {
                    authClient.signOut()
//                    _uiState.value = _uiState.value.copy(
//                        email = null
//                    )
                    _toastEffect.send(ToastMessage.SignedOut)
                    updateMiscData()
                } catch (_: Exception) {
                    _toastEffect.send(ToastMessage.CannotSignOut)
                }
            }
        }
    }

    fun onExport() {
        viewModelScope.launch {
            onNonEarlyAction {
                try {
                    storageUseCases.exportData()
                    _toastEffect.send(ToastMessage.ExportedSuccessfully)
                } catch (_: Exception) {
                    _toastEffect.send(ToastMessage.CannotExport)
                }

                updateMiscData()
            }

        }
    }

    fun onImport() {
        viewModelScope.launch {
            onNonEarlyAction {
                try {
                    storageUseCases.importData()
                    _toastEffect.send(ToastMessage.ImportedSuccessfully)
                } catch (_: Exception) {
                    _toastEffect.send(ToastMessage.CannotImport)
                }
                updateMiscData()
            }
        }
    }
}