package com.rombsquare.solocards.core.ui.utils.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rombsquare.solocards.R

@Composable
fun SimpleDialog(
    title: String,
    message: String,
    yesButton: String = stringResource(R.string.yes),
    noButton: String = stringResource(R.string.no),
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