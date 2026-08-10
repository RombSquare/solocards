package com.rombsquare.solocards.ui.screens.game.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.ui.theme.SolocardsTheme

@Composable
fun ErrorDialog(
    reason: String,
    wrongCard: Card,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        textContentColor = MaterialTheme.colorScheme.error,
        onDismissRequest = { },
        title = {Text("Some card made error...")},
        text = {
            Column {
                Text("One of your cards threw an error during the creation of quiz. Please check the code of this card:")

                Text(
                    text = "\nQuestion: ${wrongCard.question}\nAnswer: ${wrongCard.answer}",
                    color = Color.White,
                )

                Text(
                    text = "\nThe reason of error:",
                    fontWeight = FontWeight.Bold,
                )

                Text(reason)
            }

        },
        confirmButton = {

        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Okay",
                    color = Color.White
                )
            }
        }
    )
}

@Preview
@Composable
fun ErrorDialogPreview() {
    SolocardsTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            ErrorDialog(
                reason = "I don't know... :)",
                wrongCard = Card(1, 1, "What is car", "It's an animal"),
                onDismiss = {}
            )
        }

    }
}