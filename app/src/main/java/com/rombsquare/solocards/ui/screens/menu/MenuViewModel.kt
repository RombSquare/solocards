package com.rombsquare.solocards.ui.screens.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.SnackbarManager
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Section
import com.rombsquare.solocards.domain.usecases.quizzes.QuizUseCases
import com.rombsquare.solocards.ui.screens.menu.models.Dialog
import com.rombsquare.solocards.domain.models.QuizSortMethod
import com.rombsquare.solocards.domain.models.QuizSortOptions
import com.rombsquare.solocards.domain.models.SortDirection
import com.rombsquare.solocards.domain.models.ValidationResult
import com.rombsquare.solocards.domain.usecases.serializer.SerializerUseCases
import com.rombsquare.solocards.domain.usecases.validation.quiz_validation.QuizValidationUseCases
import com.rombsquare.solocards.domain.usecases.validation.tag_validation.TagValidationUseCases
import com.rombsquare.solocards.ui.screens.menu.models.SerializationType
import com.rombsquare.solocards.ui.screens.menu.models.UiEffect
import com.rombsquare.solocards.ui.screens.menu.models.UiEvent
import com.rombsquare.solocards.ui.screens.menu.models.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlin.time.Duration.Companion.milliseconds

class MenuViewModel(
    val quizUseCases: QuizUseCases,
    val tagValidationUseCases: TagValidationUseCases,
    val quizValidationUseCases: QuizValidationUseCases,
    val serializerUseCases: SerializerUseCases
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    // UiEffect
    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val currentSection = MutableStateFlow<Section>(Section.Everything)
    private val sortOptions = MutableStateFlow(
        QuizSortOptions(
            method = QuizSortMethod.ByName,
            direction = SortDirection.Ascending,
            moveFavoritesToTop = true
        )
    )
    private val searchText = MutableStateFlow("")

    // Handle a single click on a quiz
    // Normally, this click leads to Editor screen
    // But in trash, it triggers the RestoreQuizDialog
    private var quizClicked: Quiz? = null

    private var serializationType = SerializationType.Quiz

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
            combine(currentSection, searchText, sortOptions) {
                Triple(currentSection, searchText, sortOptions)
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
    
    private fun hideDialog() = setDialog(null)
    
    // Unselect if null
    private fun selectQuiz(quiz: Quiz?) {
        _uiState.value = _uiState.value.copy(
            selectedQuizId = quiz?.id
        )
    }

    private fun onSuccessValidation(
        result: ValidationResult,
        block: () -> Unit
    ) {
        if (result is ValidationResult.Success) {
            block()
        } else {
            showMessage((result as ValidationResult.Failure).reason.toString())
        }
    }

    private fun showMessage(message: String) {
        _uiState.value = _uiState.value.copy(
            toastMessage = message
        )
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.CreateQuiz -> {
                onSuccessValidation(
                    result = quizValidationUseCases.quizValidation(
                        enteredQuizName = event.name,
                        quizList = _uiState.value.quizzes
                    )
                ) {
                    hideDialog()
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

            }

            is UiEvent.SelectQuiz -> {
                selectQuiz(event.quiz)
            }

            UiEvent.UnselectQuiz -> {
                selectQuiz(null)
            }

            is UiEvent.RenameQuiz -> {
                hideDialog()
                viewModelScope.launch {
                    quizUseCases.renameQuiz(selectedQuiz, event.name)
                }
                onEvent(UiEvent.UnselectQuiz)
            }

            UiEvent.OnDeleteClicked -> {
                when (_uiState.value.section) {
                    Section.Trash -> { setDialog(Dialog.DeleteWarning) }
                    else -> {
                        hideDialog()
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
                hideDialog()

                viewModelScope.launch {
                    quizUseCases.clearTrash()
                    SnackbarManager.showMessage("Trash is cleared")
                }
            }

            UiEvent.DeleteForever -> {
                hideDialog()

                viewModelScope.launch {
                    quizUseCases.deleteQuiz(selectedQuiz)
                    SnackbarManager.showMessage("Quiz was deleted")
                }
                onEvent(UiEvent.UnselectQuiz)
            }

            UiEvent.RestoreQuiz -> {
                hideDialog()

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
                setDialog(Dialog.Tag)
            }

            is UiEvent.AddTag -> {
                onSuccessValidation(
                    tagValidationUseCases.tagValidation(
                        enteredTag = event.newTag,
                        tagListOfQuiz = selectedQuiz.tags
                    )
                ) {
                    viewModelScope.launch {
                        quizUseCases.addTagToQuiz(selectedQuiz, event.newTag)
                    }

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
                selectQuiz(null)
                viewModelScope.launch {
                    val quiz = _uiState.value.quizzes.find { it.id == event.quiz.id }!!
                    val wasArchived = quiz.isArchived

                    quizUseCases.archiveQuiz(quiz)
                    val newQuiz = _uiState.value.quizzes
                        .find { it.id == quiz.id }!!
                        .copy( isArchived = !wasArchived )

                    delay(10.milliseconds)

                    if (wasArchived) {
                        SnackbarManager.showMessage(
                            message = "Quiz is unarchived",
                            actionLabel = "Undo",
                            onAction = {
                                Log.d("SolocardsTest", "Snackbar: OnAction was called!")
                                quizUseCases.archiveQuiz(newQuiz)
                            }
                        )
                    } else {
                        SnackbarManager.showMessage(
                            message = "Quiz is archived",
                            actionLabel = "Undo",
                            onAction = {
                                Log.d("SolocardsTest", "Snackbar: OnAction was called!")
                                quizUseCases.archiveQuiz(newQuiz)
                            }
                        )
                    }

                }
            }

            UiEvent.HideDialog -> {
                hideDialog()
            }

            UiEvent.OnSortIconClicked -> {
                _uiState.value = _uiState.value.copy(
                    showSortingSheet = true
                )
            }

            is UiEvent.OnSortMethodChosen -> {
                sortOptions.value = sortOptions.value.copy(
                    method = event.sortingMethod
                )

                _uiState.value = _uiState.value.copy(
                    sortOptions = sortOptions.value,
                )
            }

            is UiEvent.OnSortDirectionChosen -> {
                sortOptions.value = sortOptions.value.copy(
                    direction = event.sortDirection
                )


                _uiState.value = _uiState.value.copy(
                    sortOptions = sortOptions.value,
                )
            }

            UiEvent.OnMoveFavoritesToTopToggle -> {
                sortOptions.value = sortOptions.value.copy(
                    moveFavoritesToTop = !sortOptions.value.moveFavoritesToTop
                )

                _uiState.value = _uiState.value.copy(
                    sortOptions = sortOptions.value,
                )
            }

            UiEvent.HideSortingSheet -> {
                _uiState.value = _uiState.value.copy(
                    showSortingSheet = false
                )
            }

            UiEvent.ExportQuizLocally -> {
                hideDialog()
                viewModelScope.launch {
                    val serializedQuiz = serializerUseCases.serializeQuiz(selectedQuiz.id)

                    _uiState.value = _uiState.value.copy(
                        serializedData = serializedQuiz
                    )

                    _effect.send(UiEffect.CreateDocument(
                        name = "quiz_${selectedQuiz.title.replace(' ', '_').lowercase()}.json"
                    ))
                }
            }

            UiEvent.ImportQuizLocally -> {
                hideDialog()
                viewModelScope.launch {
                    serializationType = SerializationType.Quiz
                    _effect.send(UiEffect.OpenDocument)
                }
            }

            UiEvent.ImportProgressLocally -> {
                hideDialog()
                viewModelScope.launch {
                    serializationType = SerializationType.Progress
                    _effect.send(UiEffect.OpenDocument)
                }
            }

            is UiEvent.ObtainImportedData -> {
                viewModelScope.launch {
                    try {
                        when (serializationType) {
                            SerializationType.Quiz -> serializerUseCases.deserializeQuiz(event.jsonString)
                            SerializationType.Progress -> serializerUseCases.deserializeProgress(event.jsonString)
                        }
                        showMessage("Loaded successfully!")
                    } catch (_: SerializationException) {
                        showMessage("Cannot open this file")
                    }
                }
            }

            UiEvent.ShareQuiz -> {
                viewModelScope.launch {
                    val serializedQuiz = serializerUseCases.serializeQuiz(selectedQuiz.id)

                    _uiState.value = _uiState.value.copy(
                        serializedData = serializedQuiz
                    )

                    _effect.send(UiEffect.ShareJson(
                        jsonString = serializedQuiz,
                        name = selectedQuiz.title
                    ))
                }
            }

            UiEvent.ExportProgressLocally -> {
                viewModelScope.launch {
                    val serializedProgress = serializerUseCases.serializeProgress()

                    _uiState.value = _uiState.value.copy(
                        serializedData = serializedProgress
                    )

                    _effect.send(UiEffect.CreateDocument(
                        "progress.json"
                    ))
                }
            }
            
            UiEvent.SettingsClicked -> {
                setDialog(Dialog.Settings)
            }
            
            UiEvent.ResetClicked -> {
                setDialog(Dialog.ResetProgress)
            }
            
            UiEvent.ResetProgress -> {
                hideDialog()
                viewModelScope.launch {
                    quizUseCases.resetQuizzes()
                }
            }

            UiEvent.OnToastShown -> {
                _uiState.value = _uiState.value.copy(
                    toastMessage = ""
                )
            }

            UiEvent.ImportClicked -> {
                setDialog(Dialog.ImportProgress)
            }

            UiEvent.ShowRenameQuizDialog -> {
                setDialog(Dialog.RenameQuiz)
            }
        }
    }
}