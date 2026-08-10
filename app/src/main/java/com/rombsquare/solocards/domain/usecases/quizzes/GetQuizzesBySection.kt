package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Section
import com.rombsquare.solocards.domain.repos.DataRepo
import com.rombsquare.solocards.ui.screens.menu.models.QuizSortingMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetQuizzesBySection(
    val repo: DataRepo
) {
    operator fun invoke(section: Section, contains: String = "", sortingMethod: QuizSortingMethod = QuizSortingMethod.ByName): Flow<List<Quiz>> {
        return repo.getAllQuizzes()
            .map { quizList ->
                var newList = when (section) {
                    Section.Everything -> quizList.filter { !it.isTrashed && !it.isArchived }
                    Section.Favorite -> quizList.filter { it.isFav && !it.isArchived && !it.isTrashed }
                    Section.Trash -> quizList.filter { it.isTrashed }
                    Section.Archive -> quizList.filter { it.isArchived && !it.isTrashed }
                    is Section.Tag -> quizList.filter { it.tags.contains(section.tag) && !it.isTrashed }
                }
                    .filter { it.title.contains(contains, ignoreCase = true) }
                    .sortedWith(
                        when (sortingMethod) {
                            QuizSortingMethod.ByName -> compareBy { it.title }
                            QuizSortingMethod.ByDateCreated -> compareBy { it.createdAt }
                            QuizSortingMethod.ByDateModified -> compareBy { it.modifiedAt }
                        }
                    )
                    .sortedBy { !it.isFav }
                    .sortedBy { it.isArchived }

                if (sortingMethod == QuizSortingMethod.ByDateModified || sortingMethod == QuizSortingMethod.ByDateCreated) {
                    newList = newList.reversed()
                }

                newList
            }
    }
}