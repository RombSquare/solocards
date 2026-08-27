package com.rombsquare.solocards.features.editor_feature.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.rombsquare.solocards.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    onHome: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
    onPlay: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.card_editor)) },
        navigationIcon = {
            IconButton(
                onClick = onHome,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back_to_menu))
            }
        },
        actions = {
            IconButton(
                onClick = onHistory
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = stringResource(R.string.game_history),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(
                onClick = onSettings
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(
                onClick = onPlay
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.play),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}