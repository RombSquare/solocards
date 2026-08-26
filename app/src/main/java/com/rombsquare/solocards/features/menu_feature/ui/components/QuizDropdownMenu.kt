package com.rombsquare.solocards.features.menu_feature.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.utils.components.TextWithIcon

@Composable
fun QuizDropdownMenu(
    onRename: () -> Unit,
    onArchive: () -> Unit,
    onExport: () -> Unit,
    onShare: () -> Unit,
    isQuizArchived: Boolean,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.open_dropdown_menu)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            DropdownMenuItem(
                text = {
                    TextWithIcon(
                        icon = { Icon(Icons.Default.Edit, stringResource(R.string.rename)) },
                        text = { Text(stringResource(R.string.rename)) }
                    )
                },
                onClick = onRename
            )

            DropdownMenuItem(
                text = {
                    TextWithIcon(
                        icon = { Icon(if (isQuizArchived) Icons.Default.Unarchive else Icons.Default.Archive, stringResource(R.string.archive_action)) },
                        text = { Text(if (isQuizArchived) stringResource(R.string.unarchive_action) else stringResource(R.string.archive_action)) }
                    )
                },
                onClick = onArchive
            )

            DropdownMenuItem(
                text = {
                    TextWithIcon(
                        icon = { Icon(Icons.Default.Upload, stringResource(R.string.export)) },
                        text = { Text(stringResource(R.string.export)) }
                    )
                },
                onClick = onExport
            )

            DropdownMenuItem(
                text = {
                    TextWithIcon(
                        icon = { Icon(Icons.Default.Share, stringResource(R.string.share)) },
                        text = { Text(stringResource(R.string.share)) }
                    )
                },
                onClick = onShare
            )
        }
    }
}
