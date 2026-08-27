package com.rombsquare.solocards.features.game_feature.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.features.game_feature.domain.models.TriAnswer
import com.rombsquare.solocards.features.game_feature.ui.models.UiState
import com.rombsquare.solocards.features.game_feature.ui.models.UserAnswer

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
                    text = stringResource(R.string.did_you_think_of_answer),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.size(8.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.No)) }
                    ) {
                        Text(
                            text = stringResource(R.string.no),
                        )
                    }

                    Spacer(Modifier.size(8.dp))

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.Maybe)) }
                    ) {
                        Text(
                            text = stringResource(R.string.half_correct),
                        )
                    }

                    Spacer(Modifier.size(8.dp))

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.Yes)) },
                    ) {
                        Text(
                            text = stringResource(R.string.yes),
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
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(0.6f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground.copy(0.75f),
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(0.75f)),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.Yes)) },

                        ) {
                        Text(
                            text = stringResource(R.string.treat_as_correct),
                            textAlign = TextAlign.Center
                        )
                    }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.Maybe)) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground.copy(0.75f),
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(0.75f)),
                    ) {
                        Text(
                            text = stringResource(R.string.treat_as_half_correct),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(Modifier.size(12.dp))
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp),
                    onClick = { onAnswer(UserAnswer.YesMaybeNo(TriAnswer.No)) }
                ) {
                    Text(stringResource(R.string.okay))
                }
            }
        }
    }
}
