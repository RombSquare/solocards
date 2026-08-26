package com.rombsquare.solocards.features.menu_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme


@Composable
fun FaqDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(stringResource(R.string.faq_screen_title))},
        text = {
            Column {

            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

@Preview
@Composable
fun WarnDialogPreview() {
    SolocardsTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            FaqDialog(
                {}
            )
        }
    }
}