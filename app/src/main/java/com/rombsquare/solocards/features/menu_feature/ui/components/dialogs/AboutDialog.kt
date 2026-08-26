package com.rombsquare.solocards.features.menu_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R

@Composable
fun AboutDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(stringResource(R.string.about))},
        text = {
            Column {
                Text(stringResource(R.string.about_descr), fontWeight = FontWeight.Bold)
                Spacer(Modifier.size(20.dp))
                Text(stringResource(R.string.about_author))
                Spacer(Modifier.size(20.dp))
                Text(stringResource(R.string.about_email))
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.okay))
            }
        }
    )
}