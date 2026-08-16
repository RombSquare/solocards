package com.rombsquare.solocards.ui.screens.editor.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.ui.theme.SolocardsTheme
import com.rombsquare.solocards.ui.utils.components.LabeledCheckbox

@Composable
fun PropsDialog(
    optionBlankString: String,
    optionCount: Int,
    allowedModes: Map<GameMode, Boolean>,
    onDismiss: (String, Int, Map<GameMode, Boolean>) -> Unit,
) {
    var optionBlackString by remember { mutableStateOf(optionBlankString) }
    var sliderValue by remember { mutableFloatStateOf(optionCount.toFloat()) }
    var isVisible by remember { mutableStateOf(false) }// Mixed mode settings

    var isFlipModeChecked by remember { mutableStateOf(allowedModes[GameMode.Flip]!!) }
    var isWritingModeChecked by remember { mutableStateOf(allowedModes[GameMode.Writing]!!) }
    var isBooleanModeChecked by remember { mutableStateOf(allowedModes[GameMode.Boolean]!!) }
    var isOptionModeChecked by remember { mutableStateOf(allowedModes[GameMode.Option]!!) }

    AlertDialog(
        onDismissRequest = {
            val newAllowedModes = mapOf(
                GameMode.Flip to isFlipModeChecked,
                GameMode.Writing to isWritingModeChecked,
                GameMode.Boolean to isBooleanModeChecked,
                GameMode.Option to isOptionModeChecked
            )

            onDismiss(
                optionBlackString,
                sliderValue.toInt(),
                newAllowedModes
            )
        },
        title = {Text("Card properties")},
        text = {
            Column {
                Text("Below you can write incorrect options for this card (separated by comma). They will appear randomly in Option and Boolean mode.\n")
                OutlinedTextField(
                    modifier = Modifier
                        .heightIn(100.dp),
                    value = optionBlackString,
                    onValueChange = { optionBlackString = it },
                    label = { Text("Option blank") },
                    placeholder = { Text("Option1, Option2, Option3, ...") },
                    singleLine = false,
                )

                Text("\nThe number of options:")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        value = sliderValue,
                        onValueChange = { sliderValue = it },

                        valueRange = 2f..5f,
                        steps = 2,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = sliderValue.toInt().toString(),
                        color = Color.White,
                    )
                }

                TextButton(
                    onClick = { isVisible = !isVisible }
                ) {
                    Text("${if (isVisible) '▼' else '▶'}   Settings for Mixed mode")
                }

                if (isVisible) {
                    Column {
                        Text("Here you can set the specific modes that will appear in Mixed mode")

                        Row {
                            LabeledCheckbox(
                                checked = isFlipModeChecked,
                                onCheckedChange = { isFlipModeChecked = it },
                                label = "Flip\nmode"
                            )

                            Spacer(Modifier.weight(1f))

                            LabeledCheckbox(
                                checked = isWritingModeChecked,
                                onCheckedChange = { isWritingModeChecked = it },
                                label = "Writing\nmode"
                            )
                        }

                        Row {
                            LabeledCheckbox(
                                checked = isBooleanModeChecked,
                                onCheckedChange = { isBooleanModeChecked = it },
                                label = "Boolean\nmode"
                            )

                            Spacer(Modifier.weight(1f))

                            LabeledCheckbox(
                                checked = isOptionModeChecked,
                                onCheckedChange = { isOptionModeChecked = it },
                                label = "Option\nmode"
                            )
                        }

                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = {
                    val newAllowedModes = mapOf(
                        GameMode.Flip to isFlipModeChecked,
                        GameMode.Writing to isWritingModeChecked,
                        GameMode.Boolean to isBooleanModeChecked,
                        GameMode.Option to isOptionModeChecked
                    )

                    onDismiss(
                        optionBlackString,
                        sliderValue.toInt(),
                        newAllowedModes
                    )
                }
            ) {
                Text("Save")
            }
        }
    )
}

@Preview
@Composable
fun PropsDialogPreview() {
    SolocardsTheme {
        Box(Modifier.fillMaxSize()) {
            PropsDialog(
                onDismiss = {_, _, _ -> },
                optionBlankString = "Cat, Dog, Integer, Loadbar, And stuff",
                optionCount = 3,
                allowedModes = mapOf(
                    GameMode.Flip to false,
                    GameMode.Writing to true,
                    GameMode.Boolean to true,
                    GameMode.Option to false
                )
            )
        }
    }
}