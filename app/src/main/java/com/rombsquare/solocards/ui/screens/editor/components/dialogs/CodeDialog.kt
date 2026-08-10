package com.rombsquare.solocards.ui.screens.editor.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
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
import com.rombsquare.solocards.ui.theme.SolocardsTheme

@Composable
fun CodeDialog(
    code: String = "",
    occurrences: Int = 1,
    onRun: (String, Int) -> Unit = {_, _ -> },
    onSave: (String, Int) -> Unit = {_, _ -> }
) {
    var code by remember { mutableStateOf(code) }
    var sliderValue by remember { mutableFloatStateOf(occurrences.toFloat()) }

    AlertDialog(
        onDismissRequest = { onSave(code, sliderValue.toInt()) },
        title = {Text("Card Script")},
        text = {
            Column {
                Text("You can generate random cards using Lua language (optional)")

                Spacer(Modifier.size(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .heightIn(200.dp),
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Code (optional)") },
                    singleLine = false,
                )

                Spacer(Modifier.size(20.dp))

                Text("The number of occurrences for this card:")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        value = sliderValue,
                        onValueChange = { sliderValue = it },

                        valueRange = 1f..15f,
                        steps = 15,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = sliderValue.toInt().toString(),
                        color = Color.White,
                    )
                }
            }

        },
        confirmButton = {
            TextButton(onClick = { onRun(code, sliderValue.toInt()) }) {
                Text("Run")
            }
        },
        dismissButton = {
            TextButton(onClick = { onSave(code, sliderValue.toInt()) }) {
                Text("Save")
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
                code = "int main()",
                occurrences = 14,
            )
        }

    }
}