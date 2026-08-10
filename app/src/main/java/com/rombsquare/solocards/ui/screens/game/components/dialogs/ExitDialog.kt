package com.rombsquare.solocards.ui.screens.game.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.ui.theme.SolocardsTheme

@Composable
fun ExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Leave the game?")},
        text = {
            Text("Are you sure you want to finish this quiz?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Finish")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun ExitDialogPreview() {
    SolocardsTheme {
        Box(Modifier.fillMaxSize()) {
            ExitDialog({},{})
        }
    }
}

