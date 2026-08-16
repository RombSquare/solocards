package com.rombsquare.solocards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.ui.screens.editor.EditorScreen
import com.rombsquare.solocards.ui.screens.editor.EditorViewModel
import com.rombsquare.solocards.ui.screens.game.GameScreen
import com.rombsquare.solocards.ui.screens.game.GameViewModel
import com.rombsquare.solocards.ui.screens.menu.MenuScreen
import com.rombsquare.solocards.ui.screens.menu.MenuViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class Editor(
    val quizId: Long
)

@Serializable
object Menu

@Serializable
data class Game(
    val quizId: Long,
    val gameMode: GameMode
)

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Menu
    ) {
        composable<Menu> {
            val viewModel: MenuViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            MenuScreen(
                onEditor = { quizId ->
                    navController.navigate(Editor(quizId))
                },
                onEvent = viewModel::onEvent,
                uiEffect = viewModel.effect,
                uiState=uiState
            )
        }

        composable<Editor> {
            val viewModel: EditorViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            EditorScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onGame = { mode, quizId ->
                    navController.navigate(Game(quizId, mode))
                },
                onHome = {
                    navController.navigate(Menu)
                }
            )
        }

        composable<Game> {
            val viewModel: GameViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            GameScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onHome = {
                    navController.navigate(Menu) {
                        popUpTo(Menu) {
                            inclusive = false
                        }
                    }
                }
            )
        }
    }
}