package com.rombsquare.solocards.ui.screens.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.SnackbarManager
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Section
import com.rombsquare.solocards.domain.usecases.quizzes.QuizUseCases
import com.rombsquare.solocards.ui.screens.menu.models.Dialog
import com.rombsquare.solocards.ui.screens.menu.models.QuizSortingMethod
import com.rombsquare.solocards.ui.screens.menu.models.UiEvent
import com.rombsquare.solocards.ui.screens.menu.models.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MenuViewModel(
    val quizUseCases: QuizUseCases
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val currentSection = MutableStateFlow<Section>(Section.Everything)
    private val sortMethod = MutableStateFlow(QuizSortingMethod.ByDateCreated)
    private val searchText = MutableStateFlow("")

    // Handle a single click on a quiz
    // Normally, this click leads to Editor screen
    // But in trash, it triggers the RestoreQuizDialog
    private var quizClicked: Quiz? = null

    val selectedQuiz: Quiz
        get() = _uiState.value.selectedQuiz!!

    init {
        observeQuizzes()

        viewModelScope.launch {
            Log.d("SolocardsTest", "App is started")
            //quizUseCases.resetQuizzes()
            //quizUseCases.generateExamples()


            _uiState.value = _uiState.value.copy(
                tags = emptyList() //quizUseCases.getAllTags()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeQuizzes() {
        viewModelScope.launch {
            combine(currentSection, searchText, sortMethod) {
                Triple(currentSection, searchText, sortMethod)
            }
            // If currentSection or searchText changed, make new flow
            .flatMapLatest { (section, search, sortMethod) ->
                quizUseCases.getQuizzesBySection(section.value, search.value, sortMethod.value)
            }
            .collect { quizList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        quizzes = quizList,
                        tags = quizUseCases.getAllTags()
                    )
                }
            }
        }
    }

    private fun setSection(section: Section) {
        currentSection.value = section
        _uiState.value = _uiState.value.copy(
            section = section,
            topBarTitle = when (section) {
                Section.Everything -> "Quizzes"
                Section.Favorite -> "Favorites"
                Section.Trash -> "Trash"
                Section.Archive -> "Archive"
                is Section.Tag -> section.tag
            }
        )
    }

    private fun setDialog(dialog: Dialog?) {
        _uiState.value = _uiState.value.copy(
            dialog = dialog
        )
    }

    // Unselect if null
    private fun selectQuiz(quiz: Quiz?) {
        _uiState.value = _uiState.value.copy(
            selectedQuizId = quiz?.id
        )
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.CreateQuiz -> {
                setDialog(null)
                viewModelScope.launch {
                    val section = _uiState.value.section
                    val quiz = Quiz(
                        title = event.name,
                        tags = if (section is Section.Tag) listOf(section.tag) else emptyList(),
                        isFav = section is Section.Favorite,
                    )

                    quizUseCases.insertQuiz(quiz)
                }
            }

            is UiEvent.SelectQuiz -> {
                selectQuiz(event.quiz)
            }

            UiEvent.UnselectQuiz -> {
                selectQuiz(null)
            }

            is UiEvent.RenameQuiz -> {
                setDialog(null)
                viewModelScope.launch {
                    quizUseCases.renameQuiz(selectedQuiz, event.name)
                }
                onEvent(UiEvent.UnselectQuiz)
            }

            UiEvent.OnDeleteClicked -> {
                when (_uiState.value.section) {
                    Section.Trash -> { setDialog(Dialog.DeleteWarning) }
                    else -> {
                        setDialog(null)
                        viewModelScope.launch {
                            quizUseCases.moveQuizToTrash(selectedQuiz)
                            SnackbarManager.showMessage("Moved to trash")
                        }
                        selectQuiz(null)
                    }
                }
            }

            is UiEvent.SelectSection -> {
                setSection(event.section)
            }

            UiEvent.FabClicked -> {
                when (uiState.value.section) {
                    Section.Trash -> { setDialog(Dialog.ClearTrash) }
                    else -> { setDialog(Dialog.CreateQuiz) }
                }
            }

            UiEvent.ClearTrash -> {
                setDialog(null)

                viewModelScope.launch {
                    quizUseCases.clearTrash()
                    SnackbarManager.showMessage("Trash is cleared")
                }
            }

            UiEvent.DeleteForever -> {
                setDialog(null)

                viewModelScope.launch {
                    quizUseCases.deleteQuiz(selectedQuiz)
                    SnackbarManager.showMessage("Quiz was deleted")
                }
                onEvent(UiEvent.UnselectQuiz)
            }

            UiEvent.RestoreQuiz -> {
                setDialog(null)

                viewModelScope.launch {
                    quizUseCases.restoreQuiz(quizClicked!!)
                    quizClicked = null
                    SnackbarManager.showMessage("Quiz was restored")
                }
            }

            is UiEvent.QuizClickedInTrash -> {
                quizClicked = event.quiz
                setDialog(Dialog.RestoreQuiz)
            }

            is UiEvent.FavClicked -> {
                viewModelScope.launch {
                    quizUseCases.changeQuizFav(event.quiz)
                }
            }

            UiEvent.TagIconClicked -> {
                setDialog(Dialog.TagDialog)
            }

            is UiEvent.AddTag -> {
                viewModelScope.launch {
                    quizUseCases.addTagToQuiz(selectedQuiz, event.newTag)
                }
            }

            is UiEvent.RemoveTag -> {
                viewModelScope.launch {
                    quizUseCases.removeTagFromQuiz(selectedQuiz, event.tagToRemove)
                }
            }

            UiEvent.OnSearchClicked -> {
                searchText.value = ""
                _uiState.value = _uiState.value.copy(
                    searchMode = !_uiState.value.searchMode,
                    searchText = ""
                )
            }

            is UiEvent.OnSearch -> {
                searchText.value = event.text
                _uiState.value = _uiState.value.copy(
                    searchText = event.text
                )
            }

            is UiEvent.OnArchived -> {
                viewModelScope.launch {
                    quizUseCases.archiveQuiz(_uiState.value.quizzes.find { it.id == event.quiz.id }!!)
                    if (event.quiz.isArchived) {
                        SnackbarManager.showMessage("Quiz is unarchived")
                    } else {
                        SnackbarManager.showMessage("Quiz is archived")
                    }

                }
            }

            UiEvent.HideDialog -> {
                setDialog(null)
            }

            UiEvent.OnSortIconClicked -> {
                _uiState.value = _uiState.value.copy(
                    showSortingSheet = true
                )
            }

            is UiEvent.OnSortOptionChosen -> {
                sortMethod.value = event.sortingMethod
                _uiState.value = _uiState.value.copy(
                    sortingMethod = event.sortingMethod,
                    showSortingSheet = false
                )
            }

            UiEvent.HideSortingSheet -> {
                _uiState.value = _uiState.value.copy(
                    showSortingSheet = false
                )
            }


            UiEvent.ShowRenameQuizDialog -> {
                setDialog(Dialog.RenameQuiz)
            }
        }
    }
}