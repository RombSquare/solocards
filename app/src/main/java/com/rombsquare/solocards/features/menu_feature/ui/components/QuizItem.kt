package com.rombsquare.solocards.features.menu_feature.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.core.domain.models.Quiz

@Composable
fun QuizItem(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    selectionMode: Boolean,
    onFavClicked: () -> Unit,
    onArchived: () -> Unit,
    isSelected: Boolean,
    swipeToDismissBoxValue: SwipeToDismissBoxValue = SwipeToDismissBoxValue.EndToStart
) {
    // Swipe state
    var swipeIsTriggered by remember(quiz.id) { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        SwipeToDismissBoxValue.Settled,
        positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold,
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.StartToEnd ||
                dismissValue == SwipeToDismissBoxValue.EndToStart) {
                if (!swipeIsTriggered) {
                    swipeIsTriggered = true
                    onArchived()
                }
            }
            false
        },
    )

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = swipeToDismissBoxValue == SwipeToDismissBoxValue.StartToEnd,
        enableDismissFromEndToStart = swipeToDismissBoxValue == SwipeToDismissBoxValue.EndToStart,
        backgroundContent = {},
    ) {
        Card(
            modifier = Modifier
                .aspectRatio(0.75f)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                ),
            colors = CardDefaults.cardColors(
                containerColor = (
                        if (!quiz.isArchived) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer
                ).copy(
                        alpha = if (!quiz.isFav) 0.4f else 0.7f
                )
            ),
            border = if(isSelected)
                BorderStroke(
                    width = 2.dp,
                    color = if (!quiz.isArchived) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)
            else null
        ) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Center),
                    text = quiz.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    onClick = onFavClicked
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = if (quiz.isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        tint = if (!quiz.isArchived) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary, // The color of heart
                        contentDescription = null
                    )
                }

            }

        }
    }
}