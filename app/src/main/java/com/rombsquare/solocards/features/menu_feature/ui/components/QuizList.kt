package com.rombsquare.solocards.features.menu_feature.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

@Composable
fun QuizList(
    modifier: Modifier = Modifier,
    quizzes: List<Quiz>,
    onQuizClick: (Quiz) -> Unit = {},
    onLongQuizClick: (Quiz) -> Unit = {},
    onFavClicked: (Quiz) -> Unit = {},
    onArchived: (Quiz) -> Unit = {},
    selectedQuiz: Quiz? = null
) {
    if (quizzes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(R.string.no_quizzes),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
        return
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = quizzes,
            key = { _, quiz -> quiz.id }
        ) { i, quiz ->
            QuizItem(
                modifier = Modifier.animateItem(),
                quiz = quiz,
                onClick = { onQuizClick(quiz) },
                onLongClick = {
                    Log.d("SolocardsTest", "Quiz long tapped: $quiz")
                    onLongQuizClick(quiz)
                },
                onFavClicked = { onFavClicked(quiz) },
                onArchived = { onArchived(quiz) },
                isSelected = (selectedQuiz == quiz),
                swipeToDismissBoxValue =
                    if (i % 2 == 0) SwipeToDismissBoxValue.EndToStart else SwipeToDismissBoxValue.StartToEnd
            )
        }
    }
}

@Preview
@Composable
fun QuizListPreview() {
    SolocardsTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            QuizList(
                modifier = Modifier.fillMaxSize(),
                quizzes = listOf(
                    Quiz(0, "Basic Algebra 2"),
                    Quiz(1, "Calculus IIII", isFav = true),
                    Quiz(2, "Linked Lists"),
                    Quiz(3, "Numbers to 100", isFav = false),
                    Quiz(4, "Multiplication table, which handles also addition by the way")
                ),
            )
        }
    }
}