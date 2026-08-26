package com.rombsquare.solocards.features.editor_feature.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.rombsquare.solocards.app.Editor
import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases.CardUseCases
import com.rombsquare.solocards.core.domain.usecases.scripting.ScriptUseCases
import com.rombsquare.solocards.core.domain.models.CardSide
import com.rombsquare.solocards.core.domain.models.CardValidationError
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.Session
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.models.ValidationResult
import com.rombsquare.solocards.core.domain.usecases.validation.card_validation.CardValidationUseCases
import com.rombsquare.solocards.core.domain.usecases.history.HistoryUseCases
import com.rombsquare.solocards.core.domain.utils.GetQuizById
import com.rombsquare.solocards.features.editor_feature.domain.usecases.quiz_settings_usecases.QuizSettingsUseCases
import com.rombsquare.solocards.features.editor_feature.ui.models.Dialog
import com.rombsquare.solocards.features.editor_feature.ui.models.SnackbarMessage
import com.rombsquare.solocards.features.editor_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.editor_feature.ui.models.UiState
import com.rombsquare.solocards.features.editor_feature.ui.models.UiEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.onSuccess
import kotlin.time.Duration.Companion.milliseconds

class EditorViewModel(
    val cardUseCases: CardUseCases,
    val scriptUseCases: ScriptUseCases,
    val historyUseCases: HistoryUseCases,
    val cardValidationUseCases: CardValidationUseCases,
    val quizSettingsUseCases: QuizSettingsUseCases,
    val getQuizById: GetQuizById,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // UiState
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    // For Navigation
    private val editor = savedStateHandle.toRoute<Editor>()
    private val quizId = editor.quizId

    // For settings
    lateinit var quiz: Quiz

    // Current quiz and card list
    //private var cardIndex: Int = 0
    private var cards: StateFlow<List<Card>> = cardUseCases.getCardsByQuiz(quizId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val gameResults: StateFlow<List<Session>> = historyUseCases.getResultsByQuiz(quizId)
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
        observeCards()

        viewModelScope.launch {

            quiz = getQuizById(quizId)!!
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

    private fun observeCards() {
        viewModelScope.launch {
            cards.collect { cardList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        cards = cardList
                    )
                }
            }
        }
    }

    private fun observeGameResults() {
        viewModelScope.launch {
            gameResults.collect { gameResultsList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        sessions = gameResultsList
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
        _uiState.update { it.copy(cardSide = CardSide.Question) } // When index changed, show question side
    }

    private fun setDialog(dialog: Dialog?) {
        _uiState.value = _uiState.value.copy(
            dialog = dialog
        )
    }

    private fun onSuccessCardValidation(
        result: ValidationResult,
        showSnackbarWhenError: Boolean = true,
        block: () -> Unit,
    ) {
        if (result is ValidationResult.Success) {
            block()
        } else {

            viewModelScope.launch {
                if (showSnackbarWhenError) {
                    _effect.send(
                        UiEffect.ShowSnackbar(
                            SnackbarMessage.OnFailedValidation(
                                (result as ValidationResult.Failure).reason as CardValidationError
                            )
                        )
                    )
                }

            }

        }
    }

    private fun moveToGame(mode: GameMode) {
        _uiState.value = _uiState.value.copy(
            moveToGame = true,
            chosenMode = mode
        )
    }

    private suspend fun onQuizModified() {
        quizSettingsUseCases.updateModifiedDate(quizId)
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
                    onQuizModified()
                    _effect.send(
                        UiEffect.ShowSnackbar(
                            SnackbarMessage.CardCreated
                        )
                    )
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
                        _effect.send(
                            UiEffect.ShowSnackbar(
                                SnackbarMessage.CannotDeleteSingleCard
                            )
                        )
                        return@launch
                    }

                    // Delete card
                    cardUseCases.deleteCard(currentCard!!)

                    // If the card was last, take prev card
                    if (cardIndex.value == cardCount-1) {
                        setCardIndex(cardIndex.value-1)
                    }

                    onQuizModified()
                    _effect.send(
                        UiEffect.ShowSnackbar(
                            SnackbarMessage.CardIsDeleted
                        )
                    )
                }
            }

            is UiEvent.OnCardTextChange -> {
                val result = cardValidationUseCases.cardTextValidation(_uiState.value.cardText, event.newText)

                onSuccessCardValidation(
                    result = result,
                    showSnackbarWhenError = false
                ) {
                    _uiState.value = _uiState.value.copy(
                        cardText = event.newText,
                        cards = _uiState.value.cards.mapIndexed { i, card ->
                            if (i == cardIndex.value) {
                                if (_uiState.value.cardSide == CardSide.Question) {
                                    card.copy(question = event.newText)
                                } else {
                                    card.copy(answer = event.newText)
                                }
                            } else {
                                card
                            }
                        }
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
                    onQuizModified()
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
                    onQuizModified()
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
                    onQuizModified()

                    delay(100.milliseconds)

                    Log.d("SolocardsTest", "This card is about to become generated: ${currentCard!!}")
                    val result = scriptUseCases.generateCard(currentCard!!)

                    result.onSuccess { card ->
                        _effect.send(
                            UiEffect.ShowSnackbar(
                                SnackbarMessage.Code(card.question, card.answer)
                            )
                        )
                    }.onFailure { e ->
                        _effect.send(
                            UiEffect.ShowSnackbar(
                                SnackbarMessage.CodeError(
                                    e.message ?: "Unknown error"
                                )
                            )
                        )
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
                    selectedSession = event.session
                )
            }

            UiEvent.DeleteSelectedGameResult -> {
                viewModelScope.launch {
                    historyUseCases.deleteGameResult(_uiState.value.selectedSession!!)
                    onQuizModified()
                }
            }

            UiEvent.DeleteAllGameResults -> {
                viewModelScope.launch {
                    historyUseCases.deleteAllByQuiz(editor.quizId)
                    setDialog(null)
                    _effect.send(
                        UiEffect.ShowSnackbar(SnackbarMessage.HistoryCleared)
                    )
                    onQuizModified()
                }
            }

            UiEvent.OpenPropsDialog -> {
                Log.d("SolocardsTest", "The allowed modes of this card: ${currentCard!!.allowedModes}")
                setDialog(Dialog.Props)
            }

            is UiEvent.UpdateCardProps -> {
                viewModelScope.launch {
                    Log.d("SolocardsTest", "New allowed modes: ${event.allowedModes}")
                    cardUseCases.updateCard(
                        currentCard!!.copy(
                            options = event.options,
                            optionCount = event.optionCount,
                            allowedModes = event.allowedModes,
                            count = event.occurrences.coerceIn(1, 100)
                        )
                    )
                    onQuizModified()
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
                    quizSettingsUseCases.updateSettings(
                        quizId = quizId,
                        showAnswer = event.quizSettings.showAnswer,
                        shuffleCards = event.quizSettings.shuffleCards,
                        swapCardSides = event.quizSettings.swapCardSides,
                        rateQuiz = event.quizSettings.rateQuiz,
                        showTreatAsCorrect = event.quizSettings.showTreatAsCorrect,
                        cardCount = event.quizSettings.cardCount.takeIf { event.quizSettings.cardCount > 0 }
                    )
                    onQuizModified()
                    quiz = getQuizById(quizId)!!

                    _uiState.value = _uiState.value.copy(quiz = quiz)
                }
            }

            UiEvent.OnScriptHelpClicked -> {
                setDialog(Dialog.ScriptHelp)
            }

            UiEvent.HideDialog -> {
                setDialog(null)
            }
        }
    }
}