package com.rombsquare.solocards.features.game_feature.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.features.game_feature.domain.models.BooleanAnswer
import com.rombsquare.solocards.features.game_feature.ui.models.UiState
import com.rombsquare.solocards.features.game_feature.ui.models.UserAnswer

// Bottom panel that is shown when uiState.cardSide = Question
@Composable
fun BottomQuestionPanel(
    uiState: UiState,
    onAnswer: (UserAnswer?) -> Unit,
    focusRequester: FocusRequester, // For Writing Mode
) {
    when (uiState.mode) {

        // Flip mode
        GameMode.Flip -> {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.flip_mode_text),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.size(8.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onAnswer(null) }
                ) {
                    Text(stringResource(R.string.show_answer))
                }
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
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester),
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    placeholder = { Text(stringResource(R.string.answer)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onAnswer(UserAnswer.Text(userAnswer))
                            userAnswer = ""
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    singleLine = true
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
                    text = stringResource(R.string.is_the_answer),
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