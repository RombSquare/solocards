package com.rombsquare.solocards.ui.screens.editor.components.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.models.Satisfaction
import com.rombsquare.solocards.domain.utils.toDateFormat
import com.rombsquare.solocards.domain.utils.toMinuteSecondFormat
import com.rombsquare.solocards.ui.theme.SolocardsTheme
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

@Composable
fun HistoryTable(
    gameResults: List<GameResult>,
    selected: GameResult?,
    onDelete: () -> Unit,
    onSelected: (GameResult?) -> Unit,
    showSatisfaction: Boolean,
) {
    val haptics = LocalHapticFeedback.current

    if (gameResults.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text("No games yet...")
        }
    }

    Column(
        modifier = Modifier
            .heightIn(min = 50.dp, max = 400.dp)
            .verticalScroll(rememberScrollState())
    ) {
        gameResults.forEachIndexed { _, game ->
            val satisColor = when (game.satisfaction) {
                Satisfaction.Awful -> Color.Red
                Satisfaction.Unsatisfied -> Color(0xFFFF8800)
                Satisfaction.Normal -> Color.Yellow
                Satisfaction.Good -> Color.Green
                Satisfaction.Perfect -> Color.Cyan
                Satisfaction.Unknown -> Color.LightGray
            }

            val borderStroke = if (game == selected) 3.dp else 1.dp
            val color = if (game == selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(borderStroke, color),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .combinedClickable(
                        onClick = {
                            if (game != selected) {
                                onSelected(null)
                            }
                        },
                        onLongClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSelected(game)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "${game.cardCount} cards",
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = game.gameTime.toMinuteSecondFormat(),
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = game.createdAt.toDateFormat(),
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = game.gameMode.string,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }


                Spacer(Modifier.weight(1f))

                if (game != selected) {
                    Column(
                        modifier = Modifier.widthIn(60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${game.score}%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        if (showSatisfaction) {
                            Text(
                                text = game.satisfaction.string,
                                color = satisColor,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = {
                            onDelete()
                            onSelected(null)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete session",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

            }

            Spacer(Modifier.height(8.dp))

        }
    }
}

@Composable
fun HistoryDialog(
    gameResults: List<GameResult>,
    selected: GameResult? = null,
    onDismiss: () -> Unit = {},
    onDelete: () -> Unit = {},
    onDeleteAll: () -> Unit = {},
    onSelected: (GameResult?) -> Unit = {},
    showSatisfaction: Boolean = true
) {
    var isDeleteAllClicked by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            onSelected(null)
            onDismiss()
        },
        title = {Text("History")},
        text = {
            HistoryTable(
                gameResults = gameResults,
                onDelete = onDelete,
                onSelected = onSelected,
                selected = selected,
                showSatisfaction = showSatisfaction
            )
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = {
                if (isDeleteAllClicked) {
                    onSelected(null)
                    onDeleteAll()
                    isDeleteAllClicked = false
                } else {
                    isDeleteAllClicked = true
                }
            }) {
                Text(
                    text = if (isDeleteAllClicked) "Are you sure?" else "Delete all",
                    color = if (isDeleteAllClicked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }

            TextButton(onClick = {
                onSelected(null)
                onDismiss()
            }) {
                Text("Dismiss")
            }
        }
    )
}

val testList = run {
    val lst = mutableListOf<GameResult>()

    repeat(10) { i ->
        lst.add(
            GameResult(
                createdAt = Clock.System.now() - (i*100000).seconds,
                quizId = 2,
                score = (0..100).random(),
                satisfaction = Satisfaction.entries.random(),
                cardCount = (5..40).random(),
                gameTime = (1..3600).random().seconds,
                gameMode = GameMode.entries.random()
            )
        )
    }

    lst
}

@Preview
@Composable
fun HistoryDialogPreview() {
    SolocardsTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            HistoryDialog(
                gameResults = testList,
                selected = testList[3]
            )
        }
    }
}