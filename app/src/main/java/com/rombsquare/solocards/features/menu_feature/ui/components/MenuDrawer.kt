package com.rombsquare.solocards.features.menu_feature.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.GetApp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.features.menu_feature.domain.models.Section
import com.rombsquare.solocards.features.menu_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.menu_feature.ui.models.UiState
import kotlinx.coroutines.launch

@Composable
fun MenuDrawer(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
    onFaq: () -> Unit,
    drawerState: DrawerState,
) {
    val scope = rememberCoroutineScope()

    Text(
        text = stringResource(R.string.app_name),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(Modifier.size(16.dp))

    MenuDrawerItem(
        icon = Icons.Outlined.Category,
        label = stringResource(R.string.quizzes),
        selected = uiState.section == Section.Everything,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Everything))
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Default.FavoriteBorder,
        label = stringResource(R.string.favs),
        selected = uiState.section == Section.Favorite,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Favorite))
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Outlined.Delete,
        label = stringResource(R.string.trash),
        selected = uiState.section == Section.Trash,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Trash))
            }
        }
    )

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    )

    Text(
        modifier = Modifier.padding(8.dp),
        text = stringResource(R.string.tags),
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
    )

    if (uiState.tags.isNotEmpty()) {
        uiState.tags.forEach { tag ->
            MenuDrawerItem(
                icon = Icons.Outlined.Folder,
                label = tag,
                selected = uiState.section == Section.Tag(tag),
                onClick = {
                    scope.launch {
                        drawerState.close()
                        onEvent(UiEvent.SelectSection(Section.Tag(tag)))
                    }
                }
            )
        }
    } else {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.descr_when_no_tags),
            color = MaterialTheme.colorScheme.primary,
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    )

    MenuDrawerItem(
        icon = Icons.Outlined.Archive,
        label = stringResource(R.string.archive),
        selected = uiState.section == Section.Archive,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Archive))
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Outlined.Settings,
        label = stringResource(R.string.settings),
        selected = false,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SettingsClicked)
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Outlined.GetApp,
        label = stringResource(R.string.import_quiz_button),
        selected = false,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.ImportQuizLocally)
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.AutoMirrored.Outlined.HelpOutline,
        label = stringResource(R.string.help),
        selected = false,
        onClick = {
            scope.launch {
                drawerState.close()
                onFaq()
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Default.Lightbulb,
        label = stringResource(R.string.about),
        selected = false,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.AboutClicked)
            }
        }
    )
}
