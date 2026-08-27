package com.rombsquare.solocards.core.ui.utils.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextWithIcon(
    icon: @Composable (() -> Unit),
    text: @Composable (() -> Unit),
    gapWidth: Dp = 12.dp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(Modifier.width(gapWidth))
        text()
    }
}