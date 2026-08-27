package com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.props_dialog

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R

@Composable
fun OptionField(
    options: List<String>,
    onOptionChange: (List<String>) -> Unit,
) {
    var newOptions by remember { mutableStateOf(options.filter { it.isNotEmpty() }.distinct()) }
    var optionToAdd by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (52.dp * newOptions.size + 80.dp).coerceIn(0.dp, 400.dp))
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()

                    if (newOptions.isNotEmpty() && listState.canScrollForward) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                0.9f to Color.Black,
                                1.0f to Color.Transparent
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    }

                },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .height(48.dp),
                    value = if(isFocused) optionToAdd else stringResource(R.string.option_field_placeholder),
                    onValueChange = { optionToAdd = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp
                    ),
                    interactionSource = interactionSource,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (optionToAdd.isNotEmpty() && !newOptions.contains(optionToAdd)) {
                                newOptions = newOptions
                                    .reversed()
                                    .plusElement(optionToAdd)
                                    .reversed()
                                optionToAdd = ""
                                onOptionChange(newOptions)
                            }
                        }
                    ),
                    singleLine = true
                )
            }

            itemsIndexed(
                items = newOptions,
                key = {_, option -> option}
            ) { i, option ->
                OptionItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .animateItem(),
                    option = option,
                    onOptionDone = { updatedOption ->
                        newOptions = newOptions.mapIndexed { index, oldOption ->
                            if (index == i) updatedOption else oldOption
                        }.distinct()
                        onOptionChange(newOptions)
                    },
                    onDelete = {
                        newOptions = newOptions.filter { it != option }
                        onOptionChange(newOptions)
                    }
                )
            }
        }
    }
}
