package com.rombsquare.solocards.ui.screens.game

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rombsquare.solocards.Game
import com.rombsquare.solocards.domain.models.BooleanAnswer
import com.rombsquare.solocards.domain.models.TriAnswer
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardSide
import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.models.GeneratedCard
import com.rombsquare.solocards.domain.models.GeneratingResult
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Satisfaction
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.history.HistoryUseCases
import com.rombsquare.solocards.domain.usecases.quizzes.QuizUseCases
import com.rombsquare.solocards.domain.usecases.scripting.ScriptUseCases
import com.rombsquare.solocards.ui.screens.game.models.Dialog
import com.rombsquare.solocards.ui.screens.game.models.UiEvent
import com.rombsquare.solocards.ui.screens.game.models.UiState
import com.rombsquare.solocards.ui.screens.game.models.UserAnswer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class GameViewModel(
    val cardUseCases: CardUseCases,
    val scriptUseCases: ScriptUseCases,
    val historyUseCases: HistoryUseCases,
    val quizUseCases: QuizUseCases,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    // Obtain quizId and current mode
    private val args = savedStateHandle.toRoute<Game>()
    val quizId = args.quizId
    val mode = args.gameMode
    var currentMode = if (mode == GameMode.Mixed) GameMode.random() else mode
    lateinit var quiz: Quiz

    lateinit var cards: List<GeneratedCard>

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    var cardIndex = 0
    var cardCount = 0
    val currentCard: GeneratedCard
        get() = cards[cardIndex]

    var score = 0.0
    var correct = 0
    var maybeCorrect = 0
    var incorrect = 0
    val percentage: Double get() = score / cardCount * 100

    // For Boolean mode
    var currentBooleanTaskAnswer: BooleanAnswer = BooleanAnswer.False
    var hypotheticalAnswer: String = "Loading..."

    init {
        viewModelScope.launch {
            quiz = quizUseCases.getQuiz(quizId)!!
            var rawCards = cardUseCases.getCardsByQuiz(quizId).first { it.isNotEmpty() }

            // Run the code for every card
            when (val result = scriptUseCases.generateQuiz(quiz, rawCards)) {
                is GeneratingResult.Success -> {
                    cards = result.cards
                    cardCount = cards.size
                    Log.d("SolocardsTest", "Generated Cards:\n$cards")
                }

                is GeneratingResult.Failure -> {
                    showError(
                        reason = result.reason,
                        wrongCard = result.wrongCard
                    )

                    return@launch
                }
            }

            _uiState.value = _uiState.value.copy(
                quiz = quiz,
                cardCount = cards.size,
                mode = currentMode
            )

            updateCurrentCard()
            if (currentMode == GameMode.Boolean) generateBooleanTask()
            if (currentMode == GameMode.Option) generateOptionTask()
            startTimer()
        }
    }

    private fun nextCard() {
        cardIndex++

        if (mode == GameMode.Mixed) {
            currentMode = cardUseCases.generateRandomMode(currentCard)
        }

        updateCurrentCard()

        if (currentMode == GameMode.Boolean) generateBooleanTask()
        if (currentMode == GameMode.Option) generateOptionTask()
    }

    private fun updateCurrentCard() {
        _uiState.value = _uiState.value.copy(
            solved = cardIndex,
            currentCard = currentCard,
            cardSide = CardSide.Question,
            cardText = currentCard.question,
            mode = currentMode
        )
    }

    private fun showAnswerSide() {
        _uiState.value = _uiState.value.copy(
            cardSide = CardSide.Answer,
            cardText = currentCard.answer
        )
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (isActive) {
                delay(1000.milliseconds)
                _uiState.value = _uiState.value.copy(
                    time = _uiState.value.time + 1.seconds
                )
            }
        }
    }

    private suspend fun saveGameResult(
        selectedSatisfaction: Satisfaction
    ) {
        historyUseCases.insertGameResult(
            GameResult(
                quizId = quizId,
                score = percentage.toInt(),
                satisfaction = selectedSatisfaction,
                cardCount = cardCount,
                gameTime = _uiState.value.time,
                gameMode = mode
            )
        )
    }

    private fun setDialog(dialog: Dialog?) {
        _uiState.value = _uiState.value.copy(
            dialog = dialog
        )
    }

    private fun generateBooleanTask() {

        // Generate random answer (true or false)
        currentBooleanTaskAnswer = BooleanAnswer.entries.random()
        hypotheticalAnswer = if (currentBooleanTaskAnswer.isTrue) currentCard.answer else currentCard.options.random()
        _uiState.value = _uiState.value.copy(
            hypotheticalAnswer = hypotheticalAnswer
        )

    }

    private fun generateOptionTask() {
        _uiState.value = _uiState.value.copy(
            options = cardUseCases.generateOptions(currentCard)
        )
    }

    private fun finishGame() {
        _uiState.value = _uiState.value.copy(
            isEnd = true
        )

        setDialog(
            Dialog.End(
                score = score,
                correct = correct,
                maybeCorrect = maybeCorrect,
                incorrect = incorrect
            )
        )
    }

    private fun goHome() {
        _uiState.value = _uiState.value.copy(
            goHome = true
        )
    }

    private fun showError(reason: String, wrongCard: Card) {
        _uiState.value = _uiState.value.copy(
            isError = true,
        )

        setDialog(
            Dialog.Error(
                reason = reason,
                wrongCard = wrongCard
            )
        )
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            UiEvent.ShowAnswer -> {}

            UiEvent.ShowSatisfactionDialog -> {

                // If rateQuiz param is enabled, show SatisfactionDialog
                if (quiz.rateQuiz) {
                    setDialog(Dialog.Satisfaction)
                } else {
                    viewModelScope.launch {
                        saveGameResult(selectedSatisfaction = Satisfaction.Unknown)
                        goHome()
                    }
                }

            }

            is UiEvent.MakeAnswer -> {
                val side = _uiState.value.cardSide
                var isCorrect = false

                when (event.answer) {

                    // For FlipMode
                    is UserAnswer.YesMaybeNo -> {
                        when (event.answer.value) {
                            TriAnswer.Yes -> {
                                score += 1
                                correct++
                                isCorrect = true
                            }
                            TriAnswer.Maybe -> {
                                score += 0.5
                                maybeCorrect++
                            }
                            TriAnswer.No -> {
                                incorrect++
                            }
                        }
                    }

                    // For WritingMode & OptionMode
                    is UserAnswer.Text -> {
                        if (event.answer.value.trim() == currentCard.answer) {
                            score += 1
                            correct++
                            isCorrect = true
                        } else {
                            if (currentMode == GameMode.Option) incorrect++
                        }
                    }

                    // For BooleanMode
                    is UserAnswer.TrueFalse -> {
                        if (event.answer.value == currentBooleanTaskAnswer) {
                            score += 1
                            correct++
                            isCorrect = true
                        } else {
                            incorrect++
                        }
                    }

                    null -> {}
                }

                // If current side is question?
                if (side.isQuestion &&
                    (currentMode == GameMode.Flip || quiz.showAnswer) &&
                    (currentMode == GameMode.Flip || !isCorrect)
                ) {
                    showAnswerSide()
                } else {

                    // Is end?
                    if (cardIndex == cards.lastIndex) {
                        finishGame()
                    } else {
                        nextCard()
                    }

                }
            }

            is UiEvent.OnSatisfactionSelect -> {
                viewModelScope.launch {
                    saveGameResult(
                        selectedSatisfaction = event.satis
                    )

                    goHome()
                }
            }

            UiEvent.FinishGame -> {
                finishGame()
            }

            UiEvent.ExitClicked -> {
                setDialog(Dialog.ExitDialog)
            }

            UiEvent.HideDialog -> {
                setDialog(null)
            }
        }
    }
}