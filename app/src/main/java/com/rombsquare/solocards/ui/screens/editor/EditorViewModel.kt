package com.rombsquare.solocards.ui.screens.editor

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rombsquare.solocards.Editor
import com.rombsquare.solocards.SnackbarManager
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.scripting.ScriptUseCases
import com.rombsquare.solocards.domain.models.CardSide
import com.rombsquare.solocards.domain.models.CardValidationResult
import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.usecases.card_validation.CardValidationUseCases
import com.rombsquare.solocards.domain.usecases.history.HistoryUseCases
import com.rombsquare.solocards.domain.usecases.quizzes.QuizUseCases
import com.rombsquare.solocards.ui.screens.editor.models.Dialog
import com.rombsquare.solocards.ui.screens.editor.models.UiEvent
import com.rombsquare.solocards.ui.screens.editor.models.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class EditorViewModel(
    val cardUseCases: CardUseCases,
    val scriptUseCases: ScriptUseCases,
    val historyUseCases: HistoryUseCases,
    val cardValidationUseCases: CardValidationUseCases,
    val quizUseCases: QuizUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // UiState
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    // For Navigation
    private val editor = savedStateHandle.toRoute<Editor>()
    private val quizId = editor.quizId

    // For settings
    lateinit var quiz: Quiz

    // Current quiz and card list
    //private var cardIndex: Int = 0
    private val cards: StateFlow<List<Card>> = cardUseCases.getCardsByQuiz(quizId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val gameResults: StateFlow<List<GameResult>> = historyUseCases.getResultsByQuiz(quizId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val cardIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    private val currentCard: Card?
        get() = cards.value.getOrNull(cardIndex.value)

    private val cardCount: Int
        get() = cards.value.size

    init {
        observeCurrentCard()
        observeGameResults()

        viewModelScope.launch {

            quiz = quizUseCases.getQuiz(quizId)!!
            _uiState.value = _uiState.value.copy(
                quizId = quizId,
                quiz = quiz
            )

            cards.collect { cardList ->
                if (cardList.isNotEmpty()) {
                    Log.d("SolocardsTest", "Card list: $cardList")
                    Log.d("SolocardsTest", "Card list in cards.value: ${cards.value}")
                    updateCurrentCard()
                    return@collect
                }
            }
        }
    }

    private fun observeGameResults() {
        viewModelScope.launch {
            gameResults.collect { gameResultsList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        gameResults = gameResultsList
                    )
                }
            }
        }
    }

    private fun observeCurrentCard() {
        viewModelScope.launch {
            cardIndex.collect { index ->
                Log.d("SolocardsTest", "Card index changed to $index")
                Log.d("SolocardsTest", "Now, the currentCard is $currentCard")
                updateCurrentCard()
            }
        }
    }

    private fun updateCurrentCard() {
        _uiState.value = _uiState.value.copy(
            currentCard = currentCard,
            cardText = when (_uiState.value.cardSide) {
                CardSide.Question -> currentCard?.question ?: "Loading..."
                CardSide.Answer -> currentCard?.answer ?: "Loading..."
            },
            cardCount = cardCount,
            index = cardIndex.value
        )
    }

    private fun setCardIndex(index: Int) {
        Log.d("SolocardsTest", "Called setCardIndex function with arg $index")
        cardIndex.value = if (index == -1) {
            cardCount-1
        } else {
            index % cardCount
        }
    }

    private fun setDialog(dialog: Dialog?) {
        _uiState.value = _uiState.value.copy(
            dialog = dialog
        )
    }

    private fun showMessage(message: String) {
        _uiState.value = _uiState.value.copy(
            message = message
        )
    }

    private fun onSuccessCardValidation(
        cardValidationResult: CardValidationResult,
        block: () -> Unit
    ) {
        if (cardValidationResult is CardValidationResult.Success) {
            block()
        } else {
            showMessage((cardValidationResult as CardValidationResult.Failure).reason.toString())
        }
    }

    fun moveToGame(mode: GameMode) {
        _uiState.value = _uiState.value.copy(
            moveToGame = true,
            chosenMode = mode
        )
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            UiEvent.NextCard -> {
                setCardIndex(cardIndex.value+1)
            }

            UiEvent.PrevCard -> {
                setCardIndex(cardIndex.value-1)
            }

            UiEvent.FlipCard -> {
                val newSide = _uiState.value.cardSide.flip()
                _uiState.value = _uiState.value.copy(
                    cardSide = newSide,
                    cardText = when (newSide) {
                        CardSide.Question -> currentCard!!.question
                        CardSide.Answer -> currentCard!!.answer
                    }
                )
            }

            UiEvent.CreateCard -> {
                viewModelScope.launch {
                    cardUseCases.createEmptyCard(quizId)
                    cardIndex.value = cardCount // Set to last index
                    quizUseCases.updateModifiedDate(quiz)
                    SnackbarManager.showMessage("New card is created")
                }
            }

            UiEvent.ShowDeleteWarning -> {
                setDialog(Dialog.DeleteWarning)
            }

            UiEvent.DeleteCard -> {
                setDialog(null)

                viewModelScope.launch {

                    // If there is only one card, it cannot be deleted
                    if (cardCount == 1) {
                        SnackbarManager.showMessage("You cannot delete a single card")
                        return@launch
                    }

                    // Delete card
                    cardUseCases.deleteCard(currentCard!!)

                    // If the card was last, take prev card
                    if (cardIndex.value == cardCount-1) {
                        setCardIndex(cardIndex.value-1)
                    }

                    quizUseCases.updateModifiedDate(quiz)
                    SnackbarManager.showMessage("Card is deleted")
                }
            }

            is UiEvent.OnCardTextChange -> {
                val result = cardValidationUseCases.cardTextValidation(_uiState.value.cardText, event.newText)

                onSuccessCardValidation(result) {
                    _uiState.value = _uiState.value.copy(
                        cardText = event.newText
                    )
                }
            }

            UiEvent.OnCardTextAccept -> {
                viewModelScope.launch {
                    cardUseCases.updateCardText(
                        card = currentCard!!,
                        newText = _uiState.value.cardText,
                        cardSide = _uiState.value.cardSide
                    )
                    quizUseCases.updateModifiedDate(quiz)
                    Log.d("SolocardsTest", "New card's text: ${_uiState.value.cardText}")
                }

            }

            is UiEvent.SaveCode -> {
                setDialog(null)

                viewModelScope.launch {
                    cardUseCases.updateCard(
                        _uiState.value.currentCard!!.copy(
                            code = event.code,
                            count = event.occurrences
                        )
                    )
                    quizUseCases.updateModifiedDate(quiz)
                }
            }

            is UiEvent.RunCode -> {
                setDialog(null)

                viewModelScope.launch {
                    cardUseCases.updateCard(
                        _uiState.value.currentCard!!.copy(
                            code = event.code,
                            count = event.occurrences
                        )
                    )
                    quizUseCases.updateModifiedDate(quiz)

                    delay(100.milliseconds)

                    Log.d("SolocardsTest", "This card is about to become generated: ${currentCard!!}")
                    val result = scriptUseCases.generateCard(currentCard!!)

                    result.onSuccess { card ->
                        SnackbarManager.showMessage("Question: ${card.question}\n\nAnswer: ${card.answer}")
                    }.onFailure { e ->
                        SnackbarManager.showMessage("Error: $e")
                    }
                }
            }

            UiEvent.OpenCodeDialog -> {
                setDialog(Dialog.Code)
            }

            UiEvent.Play -> {
                val result = cardValidationUseCases.basicCardValidation(cards.value)

                onSuccessCardValidation(result) {
                    setDialog(Dialog.Play)
                }
            }

            UiEvent.HistoryClicked -> {
                setDialog(Dialog.History)
            }

            is UiEvent.SelectGameResult -> {
                _uiState.value = _uiState.value.copy(
                    selectedGameResult = event.gameResult
                )
            }

            UiEvent.DeleteSelectedGameResult -> {
                viewModelScope.launch {
                    historyUseCases.deleteGameResult(_uiState.value.selectedGameResult!!)
                    quizUseCases.updateModifiedDate(quiz)
                }
            }

            UiEvent.DeleteAllGameResults -> {
                viewModelScope.launch {
                    historyUseCases.deleteAllByQuiz(editor.quizId)
                    setDialog(null)
                    SnackbarManager.showMessage("History cleared")
                    quizUseCases.updateModifiedDate(quiz)
                }
            }

            UiEvent.OpenPropsDialog -> {
                setDialog(Dialog.Props)
            }

            is UiEvent.UpdateCardProps -> {
                viewModelScope.launch {
                    Log.d("SolocardsTest", "New allowed modes: ${event.allowedModes}")
                    cardUseCases.updateCard(
                        currentCard!!.copy(
                            options = event.optionBlankString.split(",").map { it.trim() },
                            optionCount = event.optionCount,
                            allowedModes = event.allowedModes
                        )
                    )
                    quizUseCases.updateModifiedDate(quiz)
                }

                setDialog(null)
            }

            is UiEvent.OnModeClicked -> {
                when (event.mode) {
                    GameMode.Boolean -> {
                        val result = cardValidationUseCases.booleanModeValidation(cards.value)
                        onSuccessCardValidation(result) {
                            moveToGame(event.mode)
                        }
                    }

                    GameMode.Option -> {
                        val result = cardValidationUseCases.optionModeValidation(cards.value)
                        onSuccessCardValidation(result) {
                            moveToGame(event.mode)
                        }
                    }

                    GameMode.Mixed -> {
                        val result = cardValidationUseCases.mixedModeValidation(cards.value)
                        onSuccessCardValidation(result) {
                            moveToGame(event.mode)
                        }
                    }

                    else -> {
                        moveToGame(event.mode)
                    }
                }
            }

            UiEvent.SettingsClicked -> {
                setDialog(Dialog.Settings)
            }

            is UiEvent.UpdateSettings -> {
                setDialog(null)
                viewModelScope.launch {
                    val newQuiz = quiz.copy(
                        showAnswer = event.quizSettings.showAnswer,
                        shuffleCards = event.quizSettings.shuffleCards,
                        swapCardSides = event.quizSettings.swapCardSides,
                        rateQuiz = event.quizSettings.rateQuiz,
                        showTreatAsCorrect = event.quizSettings.showTreatAsCorrect,
                        cardCount = event.quizSettings.cardCount.takeIf { event.quizSettings.cardCount > 0 }
                    )
                    quiz = newQuiz
                    _uiState.value = _uiState.value.copy(quiz = newQuiz)
                    quizUseCases.updateQuiz(newQuiz)
                    quizUseCases.updateModifiedDate(quiz)
                }
            }

            UiEvent.ToastMessageShown -> {
                _uiState.value = _uiState.value.copy(
                    message = ""
                )
            }

            UiEvent.HideDialog -> {
                setDialog(null)
            }
        }
    }
}