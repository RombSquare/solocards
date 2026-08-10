package com.rombsquare.solocards.ui.screens.menu.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.ui.theme.SolocardsTheme
import androidx.compose.foundation.lazy.items

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
                "No quizzes in\ncurrent section",
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = quizzes.chunked(2),
            key = { chunk -> chunk.first().id }
        ) { chunk ->
            val quiz1 = chunk[0]
            val quiz2 = chunk.getOrNull(1)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                QuizItem(
                    modifier = Modifier
                        .weight(1f),
                        //.padding(horizontal = 60.dp, vertical = 4.dp),
                    quiz = quiz1,
                    onClick = { onQuizClick(quiz1) },
                    onLongClick = {
                        Log.d("SolocardsTest", "Quiz long tapped: ${quiz1}")
                        onLongQuizClick(quiz1)
                    },
                    selectionMode = (selectedQuiz != null),
                    onFavClicked = { onFavClicked(quiz1) },
                    onArchived = { onArchived(quiz1) },
                    isSelected = (selectedQuiz == quiz1),
                    swipeToDismissBoxValue = SwipeToDismissBoxValue.EndToStart
                )

                if (quiz2 != null) {
                    QuizItem(
                        modifier = Modifier
                            .weight(1f),
                            //.padding(horizontal = 60.dp, vertical = 4.dp),
                        quiz = quiz2,
                        onClick = { onQuizClick(quiz2) },
                        onLongClick = {
                            Log.d("SolocardsTest", "Quiz long tapped: ${quiz2}")
                            onLongQuizClick(quiz2)
                        },
                        selectionMode = (selectedQuiz != null),
                        onFavClicked = { onFavClicked(quiz2) },
                        onArchived = { onArchived(quiz2) },
                        isSelected = (selectedQuiz == quiz2),
                        swipeToDismissBoxValue = SwipeToDismissBoxValue.StartToEnd
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }

            }

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