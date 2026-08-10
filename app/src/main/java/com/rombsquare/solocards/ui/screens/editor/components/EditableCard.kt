package com.rombsquare.solocards.ui.screens.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.domain.models.CardSide

val cardColor = Color(0xFF1C2A27)

@Composable
fun EditableCard(
    modifier: Modifier = Modifier,
    cardSide: CardSide,
    value: String,
    onValueChange: (String) -> Unit,
    onValueAccept: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (value.isNotEmpty()) {
                Text(
                    text = if(cardSide == CardSide.Question) "Question" else "Answer",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            EditableText(
                modifier = Modifier.fillMaxWidth(),
                placeholder = if (cardSide == CardSide.Question) "Click to edit question..." else "Click to edit answer...",
                value = value,
                onValueChange = onValueChange,
                onValueAccept = onValueAccept,
            )
        }
    }
}