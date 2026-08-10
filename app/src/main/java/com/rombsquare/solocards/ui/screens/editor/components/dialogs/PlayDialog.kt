package com.rombsquare.solocards.ui.screens.editor.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.rombsquare.solocards.domain.models.GameMode

@Composable
fun PlayDialog(
    onDismiss: () -> Unit,
    onAccept: (GameMode) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Choose mode")},
        text = {
            Column {
                GameMode.entries.forEach { mode ->
                    TextButton(
                        onClick = { onAccept(mode) }
                    ) {
                        Text(
                            text = mode.string,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}