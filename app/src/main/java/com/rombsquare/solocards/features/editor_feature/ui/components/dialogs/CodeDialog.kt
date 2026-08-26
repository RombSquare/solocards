package com.rombsquare.solocards.features.editor_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

@Composable
fun CodeDialog(
    code: String = "",
    occurrences: Int = 1,
    onHelp: () -> Unit = {},
    onRun: (String, Int) -> Unit = {_, _ -> },
    onSave: (String, Int) -> Unit = {_, _ -> }
) {
    var code by remember { mutableStateOf(code) }
    var sliderValue by remember { mutableFloatStateOf(occurrences.toFloat()) }

    AlertDialog(
        onDismissRequest = { onSave(code, sliderValue.toInt()) },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.card_script))

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = onHelp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                        contentDescription = stringResource(R.string.help)
                    )
                }
            }

        },
        text = {
            Column {
                Text(stringResource(R.string.card_script_descr))

                Spacer(Modifier.size(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .heightIn(200.dp),
                    value = code,
                    onValueChange = { code = it },
                    label = { Text(stringResource(R.string.code)) },
                    singleLine = false,
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace
                    )
                )

                //Spacer(Modifier.size(20.dp))

//                Text(stringResource(R.string.occurrences_count))
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Slider(
//                        modifier = Modifier.fillMaxWidth(0.9f),
//                        value = sliderValue,
//                        onValueChange = { sliderValue = it },
//
//                        valueRange = 1f..15f,
//                        steps = 15,
//                    )
//
//                    Spacer(Modifier.weight(1f))
//
//                    Text(
//                        text = sliderValue.toInt().toString(),
//                        color = Color.White,
//                    )
//                }
            }

        },
        confirmButton = {
            TextButton(onClick = { onRun(code, sliderValue.toInt()) }) {
                Text(stringResource(R.string.run))
            }
        },
        dismissButton = {
            TextButton(onClick = { onSave(code, sliderValue.toInt()) }) {
                Text(stringResource(R.string.save))
            }
        }
    )
}

@Preview
@Composable
fun CodeDialogPreview() {
    SolocardsTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CodeDialog(
                code = "a = rand(1,10)\nb = rand(2,20)",
                occurrences = 14,
            )
        }

    }
}