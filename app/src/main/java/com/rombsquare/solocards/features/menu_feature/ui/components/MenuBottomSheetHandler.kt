package com.rombsquare.solocards.features.menu_feature.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.features.menu_feature.domain.models.QuizSortMethod
import com.rombsquare.solocards.features.menu_feature.domain.models.SortDirection
import com.rombsquare.solocards.features.menu_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.menu_feature.ui.models.UiState
import com.rombsquare.solocards.core.ui.utils.components.LabeledCheckbox
import com.rombsquare.solocards.core.ui.utils.components.LabeledRadiobutton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheetHandler(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
    sheetState: SheetState
) {
    if (uiState.showSortingSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            sheetState = sheetState,
            onDismissRequest = { onEvent(UiEvent.HideSortingSheet) },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = stringResource(R.string.sort_by),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.method == QuizSortMethod.ByName,
                    onClick = { onEvent(UiEvent.OnSortMethodChosen(QuizSortMethod.ByName)) },
                    label = stringResource(R.string.name)
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.method == QuizSortMethod.ByDateCreated,
                    onClick = { onEvent(UiEvent.OnSortMethodChosen(QuizSortMethod.ByDateCreated)) },
                    label = stringResource(R.string.date_created)
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.method == QuizSortMethod.ByDateModified,
                    onClick = { onEvent(UiEvent.OnSortMethodChosen(QuizSortMethod.ByDateModified)) },
                    label = stringResource(R.string.date_modified)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.direction == SortDirection.Ascending,
                    onClick = { onEvent(UiEvent.OnSortDirectionChosen(SortDirection.Ascending)) },
                    label = stringResource(R.string.ascending)
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.direction == SortDirection.Descending,
                    onClick = { onEvent(UiEvent.OnSortDirectionChosen(SortDirection.Descending)) },
                    label = stringResource(R.string.descending)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                LabeledCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    checked = uiState.sortOptions.moveFavoritesToTop,
                    onCheckedChange = { onEvent(UiEvent.OnMoveFavoritesToTopToggle) },
                    label = stringResource(R.string.move_favs_to_top)
                )

                Spacer(Modifier.size(20.dp))
            }
        }
    }
}