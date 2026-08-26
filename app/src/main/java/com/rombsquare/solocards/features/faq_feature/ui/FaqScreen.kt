package com.rombsquare.solocards.features.faq_feature.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

val greenAccentBlackTheme = Color(0xFF21EC69)
val greenAccentWhiteTheme = Color(0xFF1BB050)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqItem(
    question: String,
    answer: String,
) {
    Column {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(
                    color = if (isSystemInDarkTheme()) greenAccentBlackTheme else greenAccentWhiteTheme
                )) {
                    append("● ")
                }
                append(question)
            },
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.size(8.dp))

        Text(
            text = answer,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Justify
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(
    onHome: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.faq_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onHome) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back_to_menu))
                    }
                },
                actions = {}
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(horizontal = 40.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val questions = stringArrayResource(R.array.faq_questions)
                val answers = stringArrayResource(R.array.faq_answers)

                questions.indices.forEach { i ->
                    val question = questions[i]
                    val answer = answers[i]

                    FaqItem(
                        question = question,
                        answer = answer
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun FaqScreenPreview() {
    SolocardsTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            FaqScreen({})
        }
    }
}