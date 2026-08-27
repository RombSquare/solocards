package com.rombsquare.solocards.features.game_feature.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.utils.toColonFormat
import com.rombsquare.solocards.features.game_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.game_feature.ui.models.UiState

@Composable
fun GameContent(
    paddingValues: PaddingValues,
    focusRequester: FocusRequester,
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(8.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${stringResource(R.string.card_n)} ${uiState.solved+1}/${uiState.cardCount}",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(uiState.time.toColonFormat())
            }

            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { onEvent(UiEvent.ExitClicked) }
            ) {
                Icon(Icons.Default.Close, stringResource(R.string.exit))
            }
        }


        Spacer(Modifier.weight(1f))

        CardPager(
            modifier = if (uiState.mode == GameMode.Writing) {
                Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            } else {
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
            }.then(
                Modifier.animateContentSize()
            ),
            currentIndex = uiState.solved
        ) { targetIndex ->
            val card = uiState.cards.getOrNull(targetIndex)

            GameCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                cardSide = uiState.cardSide,
                question = card?.question ?: "...",
                answer = card?.answer ?: "..."
            )
        }

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .heightIn(180.dp)
                .padding(4.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomPanel(
                focusRequester = focusRequester,
                uiState = uiState,
                onEvent = onEvent,
            )
        }
    }
}