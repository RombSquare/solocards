package com.rombsquare.solocards.features.game_feature.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.features.game_feature.ui.components.DialogHandler
import com.rombsquare.solocards.features.game_feature.ui.components.GameContent
import com.rombsquare.solocards.features.game_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.game_feature.ui.models.UiState

@Composable
fun GameScreen(
    uiState: UiState = UiState(),
    onEvent: (UiEvent) -> Unit = {},
    onHome: () -> Unit = {},
) {
    if (uiState.goHome) onHome()

    BackHandler {
        onEvent(UiEvent.ExitClicked)
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    DialogHandler(onHome, uiState, onEvent)

    LaunchedEffect(uiState.mode, uiState.cardSide) {
        if (uiState.mode == GameMode.Writing) {
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            focusRequester.freeFocus()
            keyboardController?.hide()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        GameContent(
            paddingValues = paddingValues,
            focusRequester = focusRequester,
            uiState = uiState,
            onEvent = onEvent
        )
    }
}



@Preview
@Composable
fun GameScreenPreview() {
    SolocardsTheme {
        GameScreen()
    }
}