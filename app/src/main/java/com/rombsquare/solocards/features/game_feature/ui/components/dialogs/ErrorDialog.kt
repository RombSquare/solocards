package com.rombsquare.solocards.features.game_feature.ui.components.dialogs

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

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
        title = {Text(stringResource(R.string.game_error_title))},
        text = {
            Column {
                Text(stringResource(R.string.game_error_descr))

                Text(
                    text = "\n${stringResource(R.string.question)}: ${wrongCard.question}\n${stringResource(R.string.answer)}: ${wrongCard.answer}",
                    color = Color.White,
                )

                Text(
                    text = "\n${stringResource(R.string.error_reason)}",
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
                    text = stringResource(R.string.okay),
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