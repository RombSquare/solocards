package com.rombsquare.solocards.features.game_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.Satisfaction
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

@Composable
fun SatisfactionDialog(
    onSelect: (Satisfaction) -> Unit,
) {
    var sliderValue by remember { mutableFloatStateOf(3f) }

    AlertDialog(
        onDismissRequest = { onSelect(Satisfaction.Unknown) },
        title = {Text(stringResource(R.string.rate_result_title))},
        text = {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Slider(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    value = sliderValue,
                    onValueChange = { sliderValue = it },

                    valueRange = 1f..5f,
                    steps = 3,
                )

                val satisName = when (Satisfaction.entries[sliderValue.toInt()-1]) {
                    Satisfaction.Perfect -> stringResource(R.string.perfect)
                    Satisfaction.Good -> stringResource(R.string.good)
                    Satisfaction.Normal -> stringResource(R.string.normal)
                    Satisfaction.Unsatisfied -> stringResource(R.string.not_bad)
                    Satisfaction.Awful -> stringResource(R.string.awful)
                    else -> "" // User cannot choose Unknown satisfaction using a slider
                }

                Text(
                    text = satisName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = when (sliderValue.toInt()) {
                        1 -> MaterialTheme.colorScheme.error
                        5 -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSelect(Satisfaction.entries[sliderValue.toInt()-1]) }) {
                Text(stringResource(R.string.rate))
            }
        },
        dismissButton = {
            TextButton(onClick = { onSelect(Satisfaction.Unknown) }) {
                Text(stringResource(R.string.skip))
            }
        }
    )
}

@Preview
@Composable
fun SatisfactionDialogPreview() {
    SolocardsTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SatisfactionDialog(
                onSelect = {}
            )
        }
    }
}