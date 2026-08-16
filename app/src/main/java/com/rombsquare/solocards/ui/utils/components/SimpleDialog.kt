package com.rombsquare.solocards.ui.utils.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SimpleDialog(
    title: String,
    message: String,
    yesButton: String = "Yes",
    noButton: String = "No",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(title)},
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(yesButton)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(noButton)
            }
        }
    )
}