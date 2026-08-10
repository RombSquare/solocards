package com.rombsquare.solocards.ui.screens.menu.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.ui.screens.game.models.UserAnswer

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    option: String,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        shape = RectangleShape,
        onClick = onClick,
    ) {
        Text(option)
    }
}