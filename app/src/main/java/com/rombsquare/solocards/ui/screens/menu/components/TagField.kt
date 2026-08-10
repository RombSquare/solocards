package com.rombsquare.solocards.ui.screens.menu.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.ui.theme.SolocardsTheme

@Composable
fun TagField(
    tags: List<String>,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
) {
    var currentText by remember { mutableStateOf("") }

    Column {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            tags.forEach { tag ->
                key(tag) {
                    InputChip(
                        modifier = Modifier
                            .height(32.dp),
                        selected = false,
                        onClick = {},
                        label = { Text(tag) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove tag",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onRemoveTag(tag) }
                            )
                        }
                    )
                }
            }
        }

        Spacer(Modifier.size(20.dp))

        Row {
            TextField(
                modifier = Modifier.weight(1f),
                value = currentText,
                onValueChange = { currentText = it },

                label = { Text("Enter tag name") }
            )

            IconButton(
                modifier = Modifier
                    .height(60.dp)
                    .aspectRatio(1f),
                onClick = {
                    onAddTag(currentText)
                    currentText = ""
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, null)
            }
        }

    }

}

@Preview
@Composable
fun TagFieldPreview() {
    SolocardsTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TagField(
                tags = listOf("Tag1", "Cat", "Dog", "Maths", "Very long text", "Very long text", "Very long text"),
                onAddTag = {},
                onRemoveTag = {}
            )
        }
    }
}