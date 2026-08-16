package com.rombsquare.solocards

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.rombsquare.solocards.di.appModule
import com.rombsquare.solocards.ui.screens.editor.EditorScreen
import com.rombsquare.solocards.ui.theme.SolocardsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

// Snackbar manager for entire app
object SnackbarManager {
    data class MessageData(
        val message: String,
        val actionLabel: String?,
        val onAction: suspend () -> Unit
    )

    private val _messages = Channel<MessageData>(capacity = Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    suspend fun showMessage(
        message: String,
        actionLabel: String? = null,
        onAction: suspend () -> Unit = {}
    ) {
        _messages.send(
            MessageData(
                message,
                actionLabel,
                onAction
            )
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startKoin {
            androidContext(this@MainActivity)
            androidLogger()
            modules(appModule)
        }

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            // Show snackbars
            LaunchedEffect(Unit) {
                SnackbarManager.messages.collect { messageData ->
                    Log.d("SolocardsTest", "Snackbar has shown with message: ${messageData.message}")

                    val result = snackbarHostState.showSnackbar(
                        message = messageData.message,
                        duration = SnackbarDuration.Short,
                        actionLabel = messageData.actionLabel
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        Log.d("SolocardsTest", "Snackbar action was performed with message: ${messageData.message}")

                        scope.launch {
                            Log.d("SolocardsTest", "Snackbar action was performed inside scope.launch")
                            messageData.onAction()
                        }
                    }
                }
            }

            SolocardsTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(snackbarHostState) { data ->
                            Snackbar(
                                snackbarData = data,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                actionColor = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavHost()
                }
            }
        }
    }
}