package com.rombsquare.solocards.features.menu_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

@Composable
fun SettingsDialog(
    onImportProgress: () -> Unit,
    onExportProgress: () -> Unit,
    onResetProgress: () -> Unit,
    onCloud: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(stringResource(R.string.settings))},
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
                            contentDescription = stringResource(R.string.import_data_button)
                        )

                        Text(stringResource(R.string.import_data_button))
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
                            contentDescription = stringResource(R.string.export_data_button)
                        )

                        Text(stringResource(R.string.export_data_button))
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
                            contentDescription = stringResource(R.string.reset_data_button)
                        )

                        Text(stringResource(R.string.reset_data_button))
                    }
                }

                OutlinedButton(
                    onClick = onCloud
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = stringResource(R.string.cloud_storage)
                        )

                        Spacer(Modifier.size(8.dp))

                        Text(stringResource(R.string.cloud_storage))
                    }
                }
            }

        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
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
                {},
                {}
            )
        }
    }
}