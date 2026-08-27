package com.rombsquare.solocards.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.features.cloud_feature.ui.CloudDialog
import com.rombsquare.solocards.features.cloud_feature.ui.CloudViewModel
import com.rombsquare.solocards.features.editor_feature.ui.EditorScreen
import com.rombsquare.solocards.features.editor_feature.ui.EditorViewModel
import com.rombsquare.solocards.features.faq_feature.ui.FaqScreen
import com.rombsquare.solocards.features.game_feature.ui.GameScreen
import com.rombsquare.solocards.features.game_feature.ui.GameViewModel
import com.rombsquare.solocards.features.menu_feature.ui.MenuScreen
import com.rombsquare.solocards.features.menu_feature.ui.MenuViewModel
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

@Serializable
object Faq

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Menu,
    ) {

        // Menu screen
        composable<Menu> {
            val menuViewModel: MenuViewModel = koinViewModel()
            val uiState by menuViewModel.uiState.collectAsState()

            // Is cloud dialog active?
            var showCloudDialog by remember { mutableStateOf(false) }
            val cloudViewModel: CloudViewModel = koinViewModel()
            val cloudUiState by cloudViewModel.uiState.collectAsState()

            MenuScreen(
                onEditor = { quizId ->
                    navController.navigate(Editor(quizId))
                },
                onFaq = {
                    navController.navigate(Faq)
                },
                onEvent = menuViewModel::onEvent,
                uiEffect = menuViewModel.effect,
                uiState=uiState,
                onCloud = { showCloudDialog = true }
            )

            // Cloud feature
            if (showCloudDialog) {
                val webClientId = stringResource(R.string.web_client_id)

                CloudDialog(
                    onDismiss = { showCloudDialog = false },
                    uiState = cloudUiState,
                    webClientId = webClientId,
                    onSignIn = cloudViewModel::onSignIn,
                    onSignOut = cloudViewModel::onSignOut,
                    onExport = cloudViewModel::onExport,
                    onImport = cloudViewModel::onImport,
                    toastEffect = cloudViewModel.toastEffect
                )
            }

        }

        // Editor screen
        composable<Editor> {
            val viewModel: EditorViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            EditorScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                uiEffect = viewModel.effect,
                onGame = { mode, quizId ->
                    navController.navigate(Game(quizId, mode))
                },
                onHome = {
                    navController.navigate(Menu)
                }
            )
        }

        // Game screen
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

        // Help screen (FAQ)
        composable<Faq> {
            FaqScreen(
                onHome = {
                    navController.popBackStack()
                }
            )
        }
    }
}