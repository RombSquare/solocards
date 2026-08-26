package com.rombsquare.solocards.features.editor_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.core.ui.utils.components.LabeledCheckbox

data class QuizSettingsValues(
    var showAnswer: Boolean,
    var shuffleCards: Boolean,
    var swapCardSides: Boolean,
    var showTreatAsCorrect: Boolean,
    var rateQuiz: Boolean,
    var cardCount: Int
)

@Composable
fun SettingsDialog(
    quiz: Quiz,
    onDismiss: (QuizSettingsValues) -> Unit,
) {
    var showAnswer by remember { mutableStateOf(quiz.showAnswer) }
    var shuffleCards by remember { mutableStateOf(quiz.shuffleCards) }
    var swapCardSides by remember { mutableStateOf(quiz.swapCardSides) }
    var showTreatAsCorrect by remember { mutableStateOf(quiz.showTreatAsCorrect) }
    var limitCount by remember { mutableStateOf(quiz.cardCount != null) }
    var rateQuiz by remember { mutableStateOf(quiz.rateQuiz) }
    var cardCount by remember { mutableIntStateOf(quiz.cardCount ?: 0) }

    AlertDialog(
        onDismissRequest = { onDismiss(
            QuizSettingsValues(
                showAnswer,
                shuffleCards,
                swapCardSides,
                showTreatAsCorrect,
                rateQuiz,
                cardCount
            )
        )},
        title = {Text(stringResource(R.string.quiz_settings))},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                LabeledCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    checked = showAnswer,
                    onCheckedChange = { showAnswer = it },
                    label = stringResource(R.string.show_answer_when_incorrect)
                )

                LabeledCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    checked = shuffleCards,
                    onCheckedChange = { shuffleCards = it },
                    label = stringResource(R.string.shuffle_cards)
                )

                LabeledCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    checked = swapCardSides,
                    onCheckedChange = { swapCardSides = it },
                    label = stringResource(R.string.swap_questions_and_answers)
                )

                LabeledCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    checked = rateQuiz,
                    onCheckedChange = { rateQuiz = it },
                    label = stringResource(R.string.rate_result)
                )

                LabeledCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    checked = showTreatAsCorrect,
                    onCheckedChange = { showTreatAsCorrect = it },
                    label = stringResource(R.string.show_treat_as_correct)
                )

                LabeledCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    checked = limitCount,
                    onCheckedChange = { limitCount = it },
                    label = stringResource(R.string.card_limit)
                )

                if (limitCount) {
                    OutlinedTextField(
                        modifier = Modifier
                            .width(140.dp)
                            .padding(horizontal = 20.dp),
                        value = cardCount.toString(),
                        onValueChange = {
                            cardCount = if (cardCount == 0) {
                                it
                                    .filter { char -> char.isDigit() && char != '0' }
                                    .toIntOrNull() ?: 0
                            } else {
                                it
                                    .filter {char -> char.isDigit() }
                                    .take(4)
                                    .ifEmpty { "0" }
                                    .toIntOrNull() ?: 0
                            }
                        },
                        label = { Text(stringResource(R.string.count)) },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 20.sp
                        )
                    )
                }

            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = { onDismiss(
                QuizSettingsValues(
                    showAnswer,
                    shuffleCards,
                    swapCardSides,
                    showTreatAsCorrect,
                    rateQuiz,
                    cardCount
                )
            )}) {
                Text(stringResource(R.string.save))
            }
        }
    )
}

@Preview
@Composable
fun SettingsDialogPreview() {
    SolocardsTheme {
        Box(Modifier.fillMaxSize()) {
            SettingsDialog(
                onDismiss = {},
                quiz = Quiz()
            )
        }

    }
}