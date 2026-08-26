package com.rombsquare.solocards.features.cloud_feature.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.utils.toDateFormat
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.core.ui.utils.components.TextWithIcon
import com.rombsquare.solocards.features.cloud_feature.ui.models.ToastMessage
import com.rombsquare.solocards.features.cloud_feature.ui.models.UiState
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.WarnDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Instant

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun CloudDialog(
    onDismiss: () -> Unit,
    uiState: UiState,
    webClientId: String,

    onSignIn: (String) -> Unit,
    onSignOut: () -> Unit,
    onExport: () -> Unit,
    onImport: () -> Unit,

    toastEffect: Flow<ToastMessage> = flowOf()
) {
    val context = LocalContext.current
    var showImportWarningDialog by remember { mutableStateOf(false) }
    var showExportWarningDialog by remember { mutableStateOf(false) }

    if (showImportWarningDialog) {
        WarnDialog(
            message = stringResource(R.string.cloud_import_warning),
            onConfirm = {
                onImport()
                showImportWarningDialog = false
            },
            onDismiss = { showImportWarningDialog = false }
        )
    }

    if (showExportWarningDialog) {
        WarnDialog(
            message = stringResource(R.string.cloud_export_warning),
            onConfirm = {
                onExport()
                showExportWarningDialog = false
            },
            onDismiss = { showExportWarningDialog = false }
        )
    }

    LaunchedEffect(Unit) {
        toastEffect.collect { message ->
            val text = when (message) {
                ToastMessage.SignedIn -> context.getString(R.string.toast_signed_in)
                ToastMessage.SignedOut -> context.getString(R.string.toast_signed_out)
                ToastMessage.ImportedSuccessfully -> context.getString(R.string.toast_imported_successfully)
                ToastMessage.ExportedSuccessfully -> context.getString(R.string.toast_exported_successfully)

                ToastMessage.CannotSignIn -> context.getString(R.string.toast_cannot_sign_in)
                ToastMessage.CannotSignOut -> context.getString(R.string.toast_cannot_sign_out)
                ToastMessage.CannotImport -> context.getString(R.string.toast_cannot_import)
                ToastMessage.CannotExport -> context.getString(R.string.toast_cannot_export)

                ToastMessage.WaitFewSeconds -> context.getString(R.string.toast_wait_few_seconds)
            }

            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(stringResource(R.string.cloud_storage))},
        text = {
            Column {
                if (uiState.user == null) {
                    Text(stringResource(R.string.cloud_descr))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            modifier = Modifier.padding(40.dp),
                            onClick = { onSignIn(webClientId) }
                        ) {
                            TextWithIcon(
                                icon = { Icon(Icons.AutoMirrored.Filled.Login, null) },
                                text = { Text(stringResource(R.string.google_sign_in)) }
                            )
                        }
                    }

                } else {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(R.string.logged_in_as) + ":\n")
                            }

                            append(uiState.user.email)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (uiState.dateModified != Instant.DISTANT_PAST) {
                            Button(
                                onClick = {
                                    showImportWarningDialog = true
                                }
                            ) {
                                TextWithIcon(
                                    icon = { Icon(Icons.Default.CloudDownload, null) },
                                    text = { Text(stringResource(R.string.cloud_import_button)) }
                                )
                            }
                        }


                        Spacer(Modifier.size(8.dp))

                        Button(
                            onClick = {
                                if (uiState.dateModified != Instant.DISTANT_PAST) {
                                    showExportWarningDialog = true
                                } else {
                                    onExport()
                                }
                            }
                        ) {
                            TextWithIcon(
                                icon = { Icon(Icons.Default.CloudUpload, null) },
                                text = { Text(stringResource(R.string.cloud_export_button)) }
                            )
                        }

                        if (uiState.dateModified != Instant.DISTANT_PAST) {
                            Spacer(Modifier.size(20.dp))

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append(stringResource(R.string.uploaded_at) + ": ")
                                    }

                                    append(uiState.dateModified.toDateFormat())
                                }
                            )

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append(stringResource(R.string.quizzes_in_cloud) + ": ")
                                    }

                                    append(uiState.quizCount.toString())
                                }
                            )
                        }

                    }


                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Row {
                uiState.user?.let {
                    TextButton(onClick = onSignOut) {
                        Text(stringResource(R.string.sign_out))
                    }
                }

                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.close))
                }
            }
        }
    )
}

@Preview
@Composable
fun CloudDialogPreview() {
    SolocardsTheme {
        Surface(Modifier.fillMaxSize()) {
            CloudDialog(
                onDismiss = {},
                uiState = UiState(),
                webClientId = "",
                onSignIn = {},
                onSignOut = {},
                onExport = {},
                onImport = {}
            )
        }
    }
}