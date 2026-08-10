package com.rombsquare.solocards.ui.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.domain.models.BooleanAnswer
import com.rombsquare.solocards.domain.models.TriAnswer
import com.rombsquare.solocards.domain.models.CardSide
import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.domain.utils.toColonFormat
import com.rombsquare.solocards.presentation.screens.true_false_mode.components.YesNoButtons
import com.rombsquare.solocards.ui.screens.game.components.dialogs.EndDialog
import com.rombsquare.solocards.ui.screens.game.components.GameCard
import com.rombsquare.solocards.ui.screens.game.models.Dialog
import com.rombsquare.solocards.ui.screens.game.models.UiEvent
import com.rombsquare.solocards.ui.screens.game.models.UiState
import com.rombsquare.solocards.ui.screens.game.models.UserAnswer
import com.rombsquare.solocards.ui.screens.game.components.dialogs.ErrorDialog
import com.rombsquare.solocards.ui.screens.game.components.dialogs.ExitDialog
import com.rombsquare.solocards.ui.screens.menu.components.OptionButton
import com.rombsquare.solocards.ui.theme.SolocardsTheme
import com.rombsquare.solocards.ui.screens.game.components.dialogs.SatisfactionDialog

@Composable
fun GameScreen(
    uiState: UiState = UiState(),
    onEvent: (UiEvent) -> Unit = {},
    onHome: () -> Unit = {},
) {
    if (uiState.goHome) onHome()

    // Back handler
    BackHandler {
        onEvent(UiEvent.ExitClicked)
    }

    // Dialogs
    when (val dialog = uiState.dialog) {
        is Dialog.Error -> {
            ErrorDialog(
                reason = dialog.reason,
                wrongCard = dialog.wrongCard,
                onDismiss = onHome
            )
        }

        is Dialog.End -> {
            EndDialog(
                score = dialog.score,
                correct = dialog.correct,
                maybeCorrect = dialog.maybeCorrect,
                incorrect = dialog.incorrect,
                totalCards = uiState.cardCount,
                onDismiss = { onEvent(UiEvent.ShowSatisfactionDialog) }
            )
        }

        Dialog.Satisfaction -> {
            SatisfactionDialog(
                onSelect = { onEvent(UiEvent.OnSatisfactionSelect(it)) }
            )
        }

        Dialog.ExitDialog -> {
            ExitDialog(
                onConfirm = { onEvent(UiEvent.FinishGame) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        null -> {}
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Card ${uiState.solved+1}/${uiState.cardCount}",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(uiState.time.toColonFormat())
                }

                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = { onEvent(UiEvent.ExitClicked) }
                ) {
                    Icon(Icons.Default.Close, "Exit")
                }
            }


            Spacer(Modifier.weight(1f))

            GameCard(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .fillMaxWidth()
                    .padding(24.dp),
                cardSide = uiState.cardSide,
                value = uiState.cardText
            )

            Spacer(Modifier.weight(1f))


            Box(
                modifier = Modifier
                    .heightIn(180.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                when (uiState.cardSide) {
                    CardSide.Question -> {
                        BottomQuestionPanel(
                            uiState = uiState,
                            onAnswer = { onEvent(UiEvent.MakeAnswer(it)) }
                        )
                    }

                    CardSide.Answer -> {
                        BottomAnswerPanel(
                            uiState = uiState,
                            onAnswer = { onEvent(UiEvent.MakeAnswer(it)) }
                        )
                    }
                }
            }
        }
    }
}

// Bottom panel that is shown when uiState.cardSide = Question
@Composable
fun BottomQuestionPanel(
    uiState: UiState,
    onAnswer: (UserAnswer?) -> Unit,
) {
    when (uiState.mode) {

        // Flip mode
        GameMode.Flip -> {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(0.8f),
                onClick = { onAnswer(null) }
            ) {
                Text("Show answer")
            }
        }

        // Writing mode
        GameMode.Writing -> {
            var userAnswer by remember { mutableStateOf("") }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    placeholder = { Text("Answer") }
                )

                IconButton(
                    modifier = Modifier
                        .height(60.dp)
                        .aspectRatio(1f),
                    onClick = {
                        onAnswer(UserAnswer.Text(userAnswer))
                        userAnswer = ""
                    }
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, null)
                }
            }
        }

        // Boolean mode
        GameMode.Boolean -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Is the answer",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.hypotheticalAnswer,
                        textAlign = TextAlign.Center
                    )
                }


                Spacer(Modifier.size(24.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    YesNoButtons(
                        onYesClick = { onAnswer(UserAnswer.TrueFalse(BooleanAnswer.True)) },
                        onNoClick = { onAnswer(UserAnswer.TrueFalse(BooleanAnswer.False)) }
                    )
                }
            }
        }

        GameMode.Option -> {
            Column {

                if (uiState.options.size <= 3) {
                    uiState.options.forEach { option ->
                        OptionButton(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            option = option,
                            onClick = { onAnswer(UserAnswer.Text(option)) }
                        )
                    }
                } else {
                    Row(
                        Modifier.height(IntrinsicSize.Min)
                    ) {
                        OptionButton(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            option = uiState.options[0],
                            onClick = { onAnswer(UserAnswer.Text(uiState.options[0])) }
                        )

                        Spacer(Modifier.size(8.dp))

                        OptionButton(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            option = uiState.options[1],
                            onClick = { onAnswer(UserAnswer.Text(uiState.options[1])) }
                        )
                    }

                    Spacer(Modifier.size(8.dp))

                    Row(
                        Modifier.height(IntrinsicSize.Min)
                    ) {
                        OptionButton(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            option = uiState.options[2],
                            onClick = { onAnswer(UserAnswer.Text(uiState.options[2])) }
                        )

                        Spacer(Modifier.size(8.dp))

                        OptionButton(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            option = uiState.options[3],
                            onClick = { onAnswer(UserAnswer.Text(uiState.options[3])) }
                        )
                    }

                    if (uiState.options.size == 5) {
                        Spacer(Modifier.size(8.dp))

                        OptionButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            option = uiState.options[3],
                            onClick = { onAnswer(UserAnswer.Text(uiState.options[3])) }
                        )
                    }
                }
            }
        }

        GameMode.Mixed -> {}
    }
}

// Bottom panel that is shown when uiState.cardSide = Answer
@Composable
fun BottomAnswerPanel(
    uiState: UiState,
    onAnswer: (UserAnswer?) -> Unit,
) {
    when (uiState.mode) {
        GameMode.Flip -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Did you think about this answer?",
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.Yes)) },
                    ) {
                        Text(
                            text = "Yes",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Spacer(Modifier.size(8.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.Maybe)) }
                    ) {
                        Text(
                            text = "Maybe",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Spacer(Modifier.size(8.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.No)) }
                    ) {
                        Text(
                            text = "No",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }

        else -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.quiz!!.showTreatAsCorrect) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(0.6f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.LightGray,
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.Yes)) },

                        ) {
                        Text("Treat it as correct")
                    }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.Maybe)) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.LightGray,
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                    ) {
                        Text("Treat it as half-correct")
                    }

                    Spacer(Modifier.size(12.dp))
                }

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp),
                    onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.No)) }
                ) {
                    Text("Okay")
                }
            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    SolocardsTheme {
        GameScreen(
            uiState = UiState(
                cardSide = CardSide.Question,
                mode = GameMode.Option,
                hypotheticalAnswer = "The square root of the integral The square root of the integral The square root of the integral ",
                options = listOf("First", "Second", "Third I'm super super long llgdldskf;klfs;kldfsk;lsfk;l", "Fourth", "Fifth")

            )
        )
    }
}