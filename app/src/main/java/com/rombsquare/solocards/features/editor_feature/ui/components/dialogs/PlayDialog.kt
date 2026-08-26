package com.rombsquare.solocards.features.editor_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LibraryAddCheck
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.core.ui.utils.components.TextWithIcon

@Composable
fun PlayDialog(
    onDismiss: () -> Unit,
    onAccept: (GameMode) -> Unit,
) {
    var helpMode by remember { mutableStateOf(false) }

    val modeDescriptions: Map<GameMode, String> = mapOf(
        GameMode.Flip to stringResource(R.string.flip_mode_descr),
        GameMode.Writing to stringResource(R.string.writing_mode_descr),
        GameMode.Boolean to stringResource(R.string.boolean_mode_descr),
        GameMode.Option to stringResource(R.string.option_mode_descr),
        GameMode.Mixed to stringResource(R.string.mixed_mode_descr)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.choose_mode))
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = { helpMode = !helpMode }
                ) {
                    Icon(
                        imageVector = if (helpMode) Icons.AutoMirrored.Default.Help else Icons.AutoMirrored.Filled.HelpOutline,
                        contentDescription = stringResource(R.string.help)
                    )
                }
            }
        },
        text = {
            Column {
                GameMode.entries.forEach { mode ->
                    val modeName = when (mode) {
                        GameMode.Flip -> stringResource(R.string.flip_mode)
                        GameMode.Writing -> stringResource(R.string.writing_mode)
                        GameMode.Boolean -> stringResource(R.string.boolean_mode)
                        GameMode.Option -> stringResource(R.string.option_mode)
                        GameMode.Mixed -> stringResource(R.string.mixed_mode)
                    }

                    val icon = when (mode) {
                        GameMode.Flip -> Icons.Default.Loop
                        GameMode.Writing -> Icons.Default.EditNote
                        GameMode.Boolean -> Icons.Outlined.CheckCircle
                        GameMode.Option -> Icons.Default.Checklist
                        GameMode.Mixed -> Icons.Outlined.Category
                    }

                    TextButton(
                        onClick = { onAccept(mode) }
                    ) {
                        TextWithIcon(
                            text = {
                                Text(
                                    text = modeName,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = modeName,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        )
                    }

                    if (helpMode) {
                        Text(modeDescriptions[mode] ?: "")
                        Spacer(Modifier.size(8.dp))
                    }

                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}


@Preview
@Composable
fun PlayDialogPreview() {
    SolocardsTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            PlayDialog(
                onDismiss = {},
                onAccept = {}
            )
        }
    }
}