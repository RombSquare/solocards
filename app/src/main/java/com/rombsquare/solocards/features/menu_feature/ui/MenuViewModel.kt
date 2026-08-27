package com.rombsquare.solocards.features.menu_feature.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.features.menu_feature.domain.models.Section
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.QuizUseCases
import com.rombsquare.solocards.features.menu_feature.ui.models.Dialog
import com.rombsquare.solocards.features.menu_feature.domain.models.QuizSortMethod
import com.rombsquare.solocards.features.menu_feature.domain.models.QuizSortOptions
import com.rombsquare.solocards.features.menu_feature.domain.models.SortDirection
import com.rombsquare.solocards.core.domain.models.ValidationResult
import com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases.SerializerUseCases
import com.rombsquare.solocards.core.domain.usecases.validation.quiz_validation.QuizValidationUseCases
import com.rombsquare.solocards.core.domain.usecases.validation.tag_validation.TagValidationUseCases
import com.rombsquare.solocards.features.menu_feature.ui.models.SerializationType
import com.rombsquare.solocards.features.menu_feature.ui.models.SnackbarMessage
import com.rombsquare.solocards.features.menu_feature.ui.models.UiEffect
import com.rombsquare.solocards.features.menu_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.menu_feature.ui.models.UiState
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

            // It will generate example quizzes only if there are no quizzes in a local database
            quizUseCases.generateExamples()
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
            viewModelScope.launch {
                _effect.send(
                    UiEffect.ShowValidationErrorToast((result as ValidationResult.Failure).reason)
                )
            }
        }
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
                            isArchived = section is Section.Archive
                        )

                        val quizId = quizUseCases.insertQuiz(quiz)
                        _effect.send(
                            UiEffect.GoToEditor(quizId)
                        )
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
                onSuccessValidation(
                    result = quizValidationUseCases.quizValidation(
                        enteredQuizName = event.name,
                        quizList = _uiState.value.quizzes
                    )
                ) {
                    hideDialog()
                    viewModelScope.launch {
                        quizUseCases.renameQuiz(selectedQuiz, event.name)
                    }
                    onEvent(UiEvent.UnselectQuiz)
                }
            }

            UiEvent.OnDeleteClicked -> {
                when (_uiState.value.section) {
                    Section.Trash -> { setDialog(Dialog.DeleteWarning) }
                    else -> {
                        hideDialog()
                        viewModelScope.launch {
                            val quiz = selectedQuiz.copy()
                            quizUseCases.moveQuizToTrash(quiz)
                            _effect.send(
                                UiEffect.ShowSnackbar(
                                    message = SnackbarMessage.MovedToTrash,
                                    onUndo = { quizUseCases.restoreQuiz(quiz) }
                                )
                            )
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
                    _effect.send(
                        UiEffect.ShowSnackbar(SnackbarMessage.TrashIsCleared)
                    )
                }
            }

            UiEvent.DeleteForever -> {
                hideDialog()

                viewModelScope.launch {
                    quizUseCases.deleteQuiz(selectedQuiz)
                    _effect.send(
                        UiEffect.ShowSnackbar(SnackbarMessage.QuizWasDeleted)
                    )
                }
                onEvent(UiEvent.UnselectQuiz)
            }

            UiEvent.RestoreQuiz -> {
                hideDialog()

                viewModelScope.launch {
                    quizUseCases.restoreQuiz(quizClicked!!)
                    quizClicked = null
                    _effect.send(
                        UiEffect.ShowSnackbar(SnackbarMessage.QuizWasRestored)
                    )
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

                    _effect.send(
                        UiEffect.ShowSnackbar(
                            message = if (wasArchived) SnackbarMessage.QuizWasUnarchived else SnackbarMessage.QuizWasArchived,
                            onUndo = { quizUseCases.archiveQuiz(newQuiz) }
                        )
                    )
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
                        hideDialog()
                        _effect.send(
                            UiEffect.ShowSnackbar(SnackbarMessage.LoadedSuccessfully)
                        )
                    } catch (_: SerializationException) {
                        _effect.send(
                            UiEffect.ShowSnackbar(SnackbarMessage.CannotOpen)
                        )
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
                hideDialog()
                viewModelScope.launch {
                    val serializedProgress = serializerUseCases.serializeProgress()
                    _effect.send(UiEffect.ShowSnackbar(SnackbarMessage.ExportedSuccessfully))

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

            UiEvent.ImportClicked -> {
                setDialog(Dialog.ImportProgress)
            }

            UiEvent.AboutClicked -> {
                setDialog(Dialog.About)
            }

            UiEvent.ShowRenameQuizDialog -> {
                setDialog(Dialog.RenameQuiz)
            }
        }
    }
}