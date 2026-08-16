package com.rombsquare.solocards.ui.screens.menu.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.ui.theme.SolocardsTheme

@Composable
fun SettingsDialog(
    onImportProgress: () -> Unit,
    onExportProgress: () -> Unit,
    onResetProgress: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Settings")},
        text = {
            Column {
                OutlinedButton(
                    onClick = onImportProgress
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Import progress"
                        )

                        Text("Import progress")
                    }
                }

                OutlinedButton(
                    onClick = onExportProgress
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = "Export progress"
                        )

                        Text("Export progress")
                    }
                }

                OutlinedButton(
                    onClick = onResetProgress
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Reset progress (destructive!)"
                        )

                        Text("Reset progress")
                    }
                }
            }

        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Preview
@Composable
fun SettingsDialogPreview() {
    SolocardsTheme {
        Surface(Modifier.fillMaxSize()) {
            SettingsDialog(
                {},
                {},
                {},
                {}
            )
        }
    }
}