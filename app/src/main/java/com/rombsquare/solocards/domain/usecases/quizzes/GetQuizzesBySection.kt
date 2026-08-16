package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Section
import com.rombsquare.solocards.domain.repos.DataRepo
import com.rombsquare.solocards.domain.models.QuizSortMethod
import com.rombsquare.solocards.domain.models.QuizSortOptions
import com.rombsquare.solocards.domain.models.SortDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetQuizzesBySection(
    val repo: DataRepo
) {
    operator fun invoke(section: Section, contains: String = "", sortOptions: QuizSortOptions): Flow<List<Quiz>> {
        return repo.getAllQuizzes()
            .map { quizList ->

                // Filter quizzes by section
                var newList = when (section) {
                    Section.Everything -> quizList.filter { !it.isTrashed && !it.isArchived }
                    Section.Favorite -> quizList.filter { it.isFav && !it.isArchived && !it.isTrashed }
                    Section.Trash -> quizList.filter { it.isTrashed }
                    Section.Archive -> quizList.filter { it.isArchived && !it.isTrashed }
                    is Section.Tag -> quizList.filter { it.tags.contains(section.tag) && !it.isTrashed }
                }
                    // Search logic
                    .filter { it.title.contains(contains, ignoreCase = true) }

                    // Main sorting logic
                    .sortedWith(
                        when (sortOptions.method) {
                            QuizSortMethod.ByName -> compareBy { it.title.lowercase() }
                            QuizSortMethod.ByDateCreated -> compareBy { it.createdAt }
                            QuizSortMethod.ByDateModified -> compareBy { it.modifiedAt }
                        }
                    )

                // Reverse list if sorting by date modified or date created
                if (sortOptions.method == QuizSortMethod.ByDateModified || sortOptions.method == QuizSortMethod.ByDateCreated) {
                    newList = newList.reversed()
                }

                // Ascending / Descending
                if (sortOptions.direction == SortDirection.Descending) {
                    newList = newList.reversed()
                }

                // Move favorites to top
                if (sortOptions.moveFavoritesToTop) {
                    newList = newList.sortedBy { !it.isFav }
                }

                // Archived quizzes goes to the bottom
                // They are shown in tags
                newList.sortedBy { it.isArchived }
            }
    }
}