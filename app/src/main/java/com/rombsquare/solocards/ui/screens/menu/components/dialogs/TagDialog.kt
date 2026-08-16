package com.rombsquare.solocards.ui.screens.menu.components.dialogs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.ui.screens.menu.components.TagField
import com.rombsquare.solocards.ui.theme.SolocardsTheme

@Composable
fun TagDialog(
    initialTags: List<String>,
    onTagAdd: (String) -> Unit,
    onTagRemove: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Tags")},
        text = {
            TagField(
                tags = initialTags,
                onAddTag = onTagAdd,
                onRemoveTag = onTagRemove
            )

        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}

@Preview
@Composable
fun TagDialogPreview() {
    SolocardsTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TagDialog(
                initialTags = listOf("Hello", "World", "Tree", "Role", "Mathematics"),
                onTagAdd = {},
                onTagRemove = {},
                onDismiss = {}
            )
        }
    }
}