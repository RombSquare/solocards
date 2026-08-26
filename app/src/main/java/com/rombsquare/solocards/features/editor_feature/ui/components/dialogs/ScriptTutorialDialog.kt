package com.rombsquare.solocards.features.editor_feature.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

@Composable
fun ScriptTutorialDialog(
    onDismiss: () -> Unit,
) {
    var page by remember { mutableIntStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(stringResource(R.string.script_tutorial_dialog_title))},
        text = {
            when (page) {
                1 -> Page1()
                2 -> Page2()
                3 -> Page3()
                4 -> Page4()
                5 -> Page5()
                6 -> Page6()
                7 -> Page7()
                8 -> Page8()
            }
        },
        confirmButton = {
            Row {
                if (page > 1) {
                    TextButton(onClick = {
                        page--
                    }) {
                        Text(stringResource(R.string.prev))
                    }
                }

                if (page < 8) {
                    TextButton(onClick = {
                        page++
                    }) {
                        Text(stringResource(R.string.next))
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.exit))
            }
        }
    )
}

@Composable
fun Page1() {
    Column {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )) {
                    appendLine(stringResource(R.string.script_tutorial_page1_welcome)+'\n')
                }

                appendLine(stringResource(R.string.script_tutorial_page1_content))
            }
        )
    }
}

@Composable
fun Page2() {
    Column {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )) {
                    appendLine(stringResource(R.string.script_tutorial_page2_title)+'\n')
                }

                appendLine(stringResource(R.string.script_tutorial_page2_content1)+'\n')
                appendLine(stringResource(R.string.script_tutorial_page2_content2))
            }
        )
    }
}

@Composable
fun Page3() {
    Column {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )) {
                    appendLine(stringResource(R.string.script_tutorial_page3_title)+'\n')
                }

                appendLine(stringResource(R.string.script_tutorial_page3_content1)+'\n')

                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold
                )) {
                    append("${stringResource(R.string.question)}:")
                }
                appendLine("  ${stringResource(R.string.script_tutorial_page3_question)}")

                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold
                )) {
                    append("${stringResource(R.string.answer)}:")
                }
                appendLine("  {C}\n")

                appendLine(stringResource(R.string.script_tutorial_page3_content2))
            }
        )
    }
}

@Composable
fun Page4() {
    Column {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )) {
                    appendLine(stringResource(R.string.script_tutorial_page4_title)+'\n')
                }

                appendLine(stringResource(R.string.script_tutorial_page4_content1)+'\n')

                withStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontFamily.Monospace
                )) {
                    appendLine("A = rand(1, 10)")
                    appendLine("B = rand(1, 10)")
                    appendLine("C = A * B")
                }

                append("\n${stringResource(R.string.script_tutorial_page4_content2_part1)} ")
                withStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontFamily.Monospace
                )) {
                    append("rand()")
                }
                appendLine(" ${stringResource(R.string.script_tutorial_page4_content2_part2)}\n")
                appendLine(stringResource(R.string.script_tutorial_page4_content3))
            }
        )
    }
}

@Composable
fun Page5() {
    Column {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )) {
                    appendLine("${stringResource(R.string.script_tutorial_page5_title)}\n")
                }

                appendLine("${stringResource(R.string.script_tutorial_page5_content1)}\n")
                appendLine("${stringResource(R.string.script_tutorial_page5_content2)}\n")
                appendLine(stringResource(R.string.script_tutorial_page5_content3))
            }
        )
    }
}

@Composable
fun Page6() {
    Column {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )) {
                    appendLine("${stringResource(R.string.script_tutorial_page6_title)}\n")
                }

                appendLine("${stringResource(R.string.script_tutorial_page6_content1)}\n")
                appendLine("${stringResource(R.string.script_tutorial_page6_content2)}\n")

                withStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontFamily.Monospace
                )) {
                    appendLine("A = rand(1, 10)")
                    appendLine("B = rand(1, 10)")
                    appendLine("C = A * B")
                    appendLine("D = C - 1")
                }

                appendLine("\n${stringResource(R.string.script_tutorial_page6_content3)}")
            }
        )
    }
}

@Composable
fun Page7() {
    Column {
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                ) {
                    appendLine("${stringResource(R.string.script_tutorial_page7_title)}\n")
                }

                appendLine(
                    stringResource(R.string.script_tutorial_page7_content1)
                )

                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontFamily.Monospace
                    )
                ) {
                    appendLine("A = rand(1, 10)")
                    appendLine("B = rand(1, 10)")
                    appendLine("C = A * B")
                    appendLine("D = C - 1")
                    appendLine("E = C + 1")
                    appendLine("F = C - 2")
                }

                appendLine("\n${stringResource(R.string.script_tutorial_page7_content2)}")
            }
        )
    }
}

@Composable
fun Page8() {
    Column {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )) {
                    appendLine("${stringResource(R.string.script_tutorial_page8_title)}\n")
                }

                appendLine("${stringResource(R.string.script_tutorial_page8_content1)}\n")

                appendLine("${stringResource(R.string.script_tutorial_page8_content2)}\n")
                appendLine(stringResource(R.string.script_tutorial_page8_content3)+'\n')

                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontFamily.Monospace
                    )
                ) {
                    appendLine("${stringResource(R.string.script_tutorial_page8_goodluck)} ☻")
                }

            }
        )
    }
}

@Preview
@Composable
fun ScriptTutorialDialogPreview() {
    SolocardsTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ScriptTutorialDialog(
                onDismiss = {}
            )
        }
    }
}