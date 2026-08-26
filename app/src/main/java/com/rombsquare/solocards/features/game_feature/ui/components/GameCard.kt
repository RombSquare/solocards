package com.rombsquare.solocards.features.game_feature.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.CardSide

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
fun GameCardContent(
    modifier: Modifier,
    cardSide: CardSide,
    value: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (value.isNotEmpty()) {
            Text(
                text = if(cardSide == CardSide.Question) stringResource(R.string.question) else stringResource(R.string.answer),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        Text(
            modifier = Modifier.padding(4.dp),
            text = value,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    cardSide: CardSide,
    question: String,
    answer: String,
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
            .padding(16.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8f * density
            },
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 12.dp
        )
    ) {
        if (rotation <= 90f) {
            GameCardContent(
                modifier = Modifier.fillMaxSize(),
                value = question,
                cardSide = CardSide.Question,
            )
        } else {
            GameCardContent(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
                value = answer,
                cardSide = CardSide.Answer,
            )
        }
    }
}