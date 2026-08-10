package com.rombsquare.solocards.ui.screens.editor

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.ui.screens.editor.components.dialogs.CodeDialog
import com.rombsquare.solocards.ui.screens.editor.components.dialogs.WarnDialog
import com.rombsquare.solocards.ui.screens.editor.components.EditableCard
import com.rombsquare.solocards.ui.screens.editor.components.dialogs.HistoryDialog
import com.rombsquare.solocards.ui.screens.editor.components.dialogs.PlayDialog
import com.rombsquare.solocards.ui.screens.editor.components.dialogs.PropsDialog
import com.rombsquare.solocards.ui.screens.editor.components.dialogs.SettingsDialog
import com.rombsquare.solocards.ui.screens.editor.models.Dialog
import com.rombsquare.solocards.ui.screens.editor.models.UiEvent
import com.rombsquare.solocards.ui.screens.editor.models.UiState
import com.rombsquare.solocards.ui.theme.SolocardsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    uiState: UiState = UiState(),
    onEvent: (UiEvent) -> Unit = {},
    onGame: (GameMode, Long) -> Unit = {_, _ ->},
    onHome: () -> Unit = {},
) {
    val context = LocalContext.current

    if (uiState.moveToGame) {
        onGame(uiState.chosenMode, uiState.quizId!!)
    }

    LaunchedEffect(uiState.message) {
        if (uiState.message.isNotEmpty()) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            onEvent(UiEvent.ToastMessageShown)
        }
    }

    when (uiState.dialog) {
        Dialog.Code -> {
            CodeDialog(
                code = uiState.currentCard!!.code,
                occurrences = uiState.currentCard.count,
                onSave = { code, occurrences ->
                    onEvent(UiEvent.SaveCode(code, occurrences))
                },
                onRun = { code, occurrences ->
                    onEvent(UiEvent.RunCode(code, occurrences))
                }
            )
        }

        Dialog.DeleteWarning -> {
            WarnDialog(
                message = "Do you want to delete this card?",
                onConfirm = { onEvent(UiEvent.DeleteCard) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.Play -> {
            PlayDialog(
                onAccept = { mode ->
                    onEvent(UiEvent.OnModeClicked(mode))
                },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.History -> {
            HistoryDialog(
                gameResults = uiState.gameResults,
                selected = uiState.selectedGameResult,
                showSatisfaction = uiState.quiz.rateQuiz,
                onSelected = { onEvent(UiEvent.SelectGameResult(it)) },
                onDelete = { onEvent(UiEvent.DeleteSelectedGameResult) },
                onDeleteAll = { onEvent(UiEvent.DeleteAllGameResults) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.Props -> {
            PropsDialog(
                onDismiss = { optionBlankString, optionCount, allowedModes ->
                    onEvent(UiEvent.UpdateCardProps(
                        optionBlankString,
                        optionCount,
                        allowedModes
                    ))
                },
                optionBlankString = uiState.currentCard!!.options.fastJoinToString(", "),
                optionCount = uiState.currentCard.optionCount,
                allowedModes = uiState.currentCard.allowedModes
            )
        }

        Dialog.Settings -> {
            SettingsDialog(
                quiz = uiState.quiz,
                onDismiss = { onEvent(UiEvent.UpdateSettings(it)) },
            )
        }

        null -> {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card Editor") },
                navigationIcon = {
                    IconButton(
                        onClick = onHome,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back to Menu")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(UiEvent.HistoryClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = { onEvent(UiEvent.SettingsClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = { onEvent(UiEvent.Play) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    EditableCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .fillMaxHeight(0.6f),
                        cardSide = uiState.cardSide,
                        value = uiState.cardText,
                        onValueChange = {
                            onEvent(UiEvent.OnCardTextChange(it))
                        },
                        onValueAccept = {
                            onEvent(UiEvent.OnCardTextAccept)
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onEvent(UiEvent.FlipCard) }
                        ) {
                            Text("Flip card")
                        }

                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onEvent(UiEvent.OpenPropsDialog) }
                        ) {
                            Text("Properties")
                        }

                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onEvent(UiEvent.OpenCodeDialog) }
                        ) {
                            Text("Edit code")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = { onEvent(UiEvent.CreateCard) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.tertiary,
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
                            ) {
                                Text("Create card")
                            }

                            Spacer(Modifier.width(4.dp))

                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = { onEvent(UiEvent.ShowDeleteWarning) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.tertiary,
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)

                            ) {
                                Text("Delete card")
                            }
                        }
                    }



                    Spacer(Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onEvent(UiEvent.PrevCard) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous card",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = "${uiState.index+1}/${uiState.cardCount}",
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(Modifier.weight(1f))

                        IconButton(
                            onClick = { onEvent(UiEvent.NextCard) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Previous card",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

        }
    )

}

@Preview(
    showBackground = false,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun EditorScreenPreview() {
    SolocardsTheme {
        EditorScreen()
    }
}