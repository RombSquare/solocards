package com.rombsquare.solocards.features.game_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

@Composable
fun EndDialog(
    score: Double,
    correct: Int,
    maybeCorrect: Int,
    incorrect: Int,
    totalCards: Int,
    onDismiss: () -> Unit,
) {
    var percentage by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        percentage = (score / totalCards * 100).toInt()
    }

    AlertDialog(
        onDismissRequest = { },
        title = {Text(stringResource(R.string.test_is_finished))},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.your_result))
                Text(
                    text = "$percentage%",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 40.sp,
                )

                Spacer(Modifier.size(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    )  {
                        Text(stringResource(R.string.correct))
                        Text(
                            text = "$correct",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 32.sp,
                        )
                    }

                    Spacer(Modifier.size(20.dp))

                    if (maybeCorrect != 0) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        )  {
                            Text(
                                text = stringResource(R.string.half_correct),
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = "$maybeCorrect",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 32.sp,
                            )
                        }

                        Spacer(Modifier.size(20.dp))
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    )  {
                        Text(
                            text = stringResource(R.string.incorrect),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "$incorrect",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 32.sp,
                        )
                    }
                }

            }

        },
        confirmButton = {

        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.home)) }
        }
    )
}

@Preview
@Composable
fun EndDialogPreview() {
    SolocardsTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            EndDialog(
                score = 7.5,
                correct = 5,
                maybeCorrect = 3,
                incorrect = 8,
                totalCards = 21,
                onDismiss = {}
            )
        }
    }
}