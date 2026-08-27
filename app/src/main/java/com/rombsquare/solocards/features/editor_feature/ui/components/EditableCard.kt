package com.rombsquare.solocards.features.editor_feature.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.CardSide
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

// The content for a single side of a card
@Composable
fun EditableCardContent(
    modifier: Modifier = Modifier,
    cardSide: CardSide,
    value: String,
    onValueChange: (String) -> Unit,
    onValueAccept: () -> Unit,
    onModify: () -> Unit,
    onDelete: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (value.isNotEmpty()) {
                Text(
                    text = if(cardSide == CardSide.Question) stringResource(R.string.question) else stringResource(R.string.answer),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            EditableText(
                modifier = Modifier.fillMaxWidth(),
                placeholder = if (cardSide == CardSide.Question) stringResource(R.string.click_to_edit_question) else stringResource(R.string.click_to_edit_answer),
                value = value,
                onValueChange = onValueChange,
                onValueAccept = onValueAccept,
            )
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            onClick = onDelete
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_card),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
            )
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .scale(scaleX = -1f, scaleY = 1f),
            onClick = onModify
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = stringResource(R.string.card_props),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
fun CardPager(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    card: @Composable ((Int) -> Unit)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = {
                if (targetState > initialState) {

                    // Forward movement
                    slideInHorizontally(animationSpec = tween(300)) { width -> width } +
                            fadeIn(animationSpec = tween(300)) togetherWith
                            slideOutHorizontally(animationSpec = tween(300)) { width -> -width } +
                            fadeOut(animationSpec = tween(300))
                } else {

                    // Backward movement
                    slideInHorizontally(animationSpec = tween(300)) { width -> -width } +
                            fadeIn(animationSpec = tween(300)) togetherWith
                            slideOutHorizontally(animationSpec = tween(300)) { width -> width } +
                            fadeOut(animationSpec = tween(300))
                }
            },
            label = "CardTransition"
        ) { targetIndex ->
            card(targetIndex)
        }
    }
}

@Composable
fun EditableCard(
    modifier: Modifier = Modifier,
    cardSide: CardSide,
    question: String,
    answer: String,
    onValueChange: (String) -> Unit,
    onValueAccept: () -> Unit,
    onDelete: () -> Unit,
    onModify: () -> Unit,
) {
    var isFlipped by remember { mutableStateOf(false) }

    LaunchedEffect(cardSide) {
        isFlipped = cardSide == CardSide.Answer
    }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "cardFlip"
    )

    ElevatedCard(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8f * density
            },
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp)
    ) {
        if (rotation <= 90f) {
            EditableCardContent(
                cardSide = CardSide.Question,
                value = question,
                onValueChange = onValueChange,
                onValueAccept = onValueAccept,
                onModify = onModify,
                onDelete = onDelete
            )
        } else {
            EditableCardContent(
                modifier = Modifier.graphicsLayer { rotationY = 180f },
                cardSide = CardSide.Answer,
                value = answer,
                onValueChange = onValueChange,
                onValueAccept = onValueAccept,
                onModify = onModify,
                onDelete = onDelete
            )
        }

    }
}