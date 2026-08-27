package com.rombsquare.solocards.features.editor_feature.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.utils.components.TextWithIcon
import com.rombsquare.solocards.features.editor_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.editor_feature.ui.models.UiState

@Composable
fun EditorContent(
    paddingValues: PaddingValues,
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            CardPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                currentIndex = uiState.index
            ) { targetIndex ->
                val card = uiState.cards.getOrNull(targetIndex)

                EditableCard(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    cardSide = uiState.cardSide,
                    question = card?.question ?: "",
                    answer = card?.answer ?: "",
                    onValueChange = {
                        onEvent(UiEvent.OnCardTextChange(it))
                    },
                    onValueAccept = {
                        onEvent(UiEvent.OnCardTextAccept)
                    },
                    onDelete = { onEvent(UiEvent.ShowDeleteWarning) },
                    onModify = { onEvent(UiEvent.OpenPropsDialog) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.height(60.dp),
                    onClick = { onEvent(UiEvent.FlipCard) }
                ) {
                    TextWithIcon(
                        text = { Text(stringResource(R.string.flip_card)) },
                        icon = { Icon(Icons.Default.Loop, stringResource(R.string.flip_card)) }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onEvent(UiEvent.PrevCard) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.prev_card),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = "${uiState.index+1}/${uiState.cardCount}",
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.weight(1f))

                if (uiState.index + 1 == uiState.cardCount) {
                    IconButton(
                        onClick = { onEvent(UiEvent.CreateCard) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.create_card),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    IconButton(
                        onClick = { onEvent(UiEvent.NextCard) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.next_card),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}