package com.rombsquare.solocards.features.game_feature.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    option: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        shape = RectangleShape,
        onClick = onClick,
    ) {
        Text(option)
    }
}