package com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.props_dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.core.ui.utils.components.LabeledCheckbox

// Util function that returns a list of allowed modes
fun getListOfModes(
    isFlipModeChecked: Boolean,
    isWritingModeChecked: Boolean,
    isBooleanModeChecked: Boolean,
    isOptionModeChecked: Boolean
): List<GameMode> {
    val newAllowedModes = mutableListOf<GameMode>()
    if (isFlipModeChecked) { newAllowedModes.add(GameMode.Flip) }
    if (isWritingModeChecked) { newAllowedModes.add(GameMode.Writing) }
    if (isBooleanModeChecked) { newAllowedModes.add(GameMode.Boolean) }
    if (isOptionModeChecked) { newAllowedModes.add(GameMode.Option) }

    // If there are no modes, add flip mode by default
    if (newAllowedModes.isEmpty()) { newAllowedModes.add(GameMode.Flip) }

    return newAllowedModes.toList()
}

@Composable
fun PropsDialog(
    options: List<String>,
    optionCount: Int,
    allowedModes: List<GameMode>,
    occurrences: Int,
    onCode: () -> Unit,
    onDismiss: (List<String>, Int, Int, List<GameMode>) -> Unit,
) {
    var newOptions by remember { mutableStateOf(options) }
    var sliderValue by remember { mutableFloatStateOf(optionCount.toFloat()) }
    var currentSection by remember { mutableStateOf<AccordionSection?>(AccordionSection.OptionBlank) }// Mixed mode settings
    var enteredOccurrences by remember { mutableIntStateOf(occurrences) }

    var isFlipModeChecked by remember { mutableStateOf(allowedModes.contains(GameMode.Flip)) }
    var isWritingModeChecked by remember { mutableStateOf(allowedModes.contains(GameMode.Writing)) }
    var isBooleanModeChecked by remember { mutableStateOf(allowedModes.contains(GameMode.Boolean)) }
    var isOptionModeChecked by remember { mutableStateOf(allowedModes.contains(GameMode.Option)) }

    AlertDialog(
        onDismissRequest = {
            onDismiss(
                newOptions,
                sliderValue.toInt(),
                enteredOccurrences,
                getListOfModes(
                    isFlipModeChecked,
                    isWritingModeChecked,
                    isBooleanModeChecked,
                    isOptionModeChecked
                )
            )
        },
        title = {Text(stringResource(R.string.card_props))},
        text = {
            Column {
                AccordionToggler(
                    section = AccordionSection.OptionBlank,
                    activeSection = currentSection,
                    label = stringResource(R.string.option_blank),
                    onClick = { currentSection = it },
                    helpMessage = stringResource(R.string.option_blank_help)
                ) {
                    Column {
                        OptionField(
                            options = newOptions,
                            onOptionChange = { newOptions = it }
                        )
                    }
                }

                AccordionToggler(
                    section = AccordionSection.OptionCount,
                    activeSection = currentSection,
                    label = stringResource(R.string.option_count),
                    onClick = { currentSection = it },
                    helpMessage = stringResource(R.string.option_count_help)
                ) {
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
                }

                AccordionToggler(
                    section = AccordionSection.Occurrences,
                    activeSection = currentSection,
                    label = stringResource(R.string.occurrences),
                    onClick = { currentSection = it },
                    helpMessage = stringResource(R.string.occurrences_descr)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .width(140.dp)
                            .padding(horizontal = 20.dp),
                        value = enteredOccurrences.toString(),
                        onValueChange = {
                            enteredOccurrences = if (occurrences == 0) {
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

                AccordionToggler(
                    section = AccordionSection.MixedModeSettings,
                    activeSection = currentSection,
                    label = stringResource(R.string.settings_for_mixed_mode),
                    onClick = { currentSection = it },
                    helpMessage = stringResource(R.string.mixed_mode_settings_help)
                ) {
                    Column {
                        //Text(stringResource(R.string.mixed_mode_settings_descr))
                        LabeledCheckbox(
                            checked = isFlipModeChecked,
                            onCheckedChange = { isFlipModeChecked = it },
                            label = stringResource(R.string.flip_mode)
                        )

                        LabeledCheckbox(
                            checked = isWritingModeChecked,
                            onCheckedChange = { isWritingModeChecked = it },
                            label = stringResource(R.string.writing_mode)
                        )

                        LabeledCheckbox(
                            checked = isBooleanModeChecked,
                            onCheckedChange = { isBooleanModeChecked = it },
                            label = stringResource(R.string.boolean_mode)
                        )

                        LabeledCheckbox(
                            checked = isOptionModeChecked,
                            onCheckedChange = { isOptionModeChecked = it },
                            label = stringResource(R.string.option_mode)
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Row {
                TextButton(
                    onClick = {
                        onDismiss(
                            newOptions,
                            sliderValue.toInt(),
                            enteredOccurrences,
                            getListOfModes(
                                isFlipModeChecked,
                                isWritingModeChecked,
                                isBooleanModeChecked,
                                isOptionModeChecked
                            )
                        )

                        onCode()
                    },
                ) {
                    Text(stringResource(R.string.edit_code))
                }

                TextButton(
                    onClick = {
                        onDismiss(
                            newOptions,
                            sliderValue.toInt(),
                            enteredOccurrences,
                            getListOfModes(
                                isFlipModeChecked,
                                isWritingModeChecked,
                                isBooleanModeChecked,
                                isOptionModeChecked
                            )
                        )
                    }
                ) {
                    Text(stringResource(R.string.close))
                }
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
                onDismiss = {_, _, _, _ -> },
                options = listOf("Cat", "Dog", "Seven", "Stuff"),
                optionCount = 3,
                occurrences = 5,
                onCode = {},
                allowedModes = listOf(GameMode.Flip, GameMode.Boolean)
            )
        }
    }
}