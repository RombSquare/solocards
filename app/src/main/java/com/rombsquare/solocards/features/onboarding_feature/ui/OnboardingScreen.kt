package com.rombsquare.solocards.features.onboarding_feature.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import kotlinx.coroutines.launch

val greenAccentBlackTheme = Color(0xFF21EC69)
val greenAccentWhiteTheme = Color(0xFF1BB050)

@Composable
fun OnboardingScreen(
    onClose: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { 6 })
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val pagerModifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()

                    when (page) {
                        0 -> OnboardingPage(
                            modifier = pagerModifier,
                            title = stringResource(R.string.onboarding_page1_title),
                            content = stringResource(R.string.onboarding_page1_content),
                            icon = null
                        )

                        1 -> OnboardingPage(
                            modifier = pagerModifier,
                            title = stringResource(R.string.onboarding_page2_title),
                            content = stringResource(R.string.onboarding_page2_content),
                            icon = Icons.Default.Bolt
                        )

                        2 -> OnboardingPage(
                            modifier = pagerModifier,
                            title = stringResource(R.string.onboarding_page3_title),
                            content = stringResource(R.string.onboarding_page3_content),
                            icon = Icons.Default.Category
                        )

                        3 -> OnboardingPage(
                            modifier = pagerModifier,
                            title = stringResource(R.string.onboarding_page4_title),
                            content = stringResource(R.string.onboarding_page4_content),
                            icon = Icons.Default.Code
                        )

                        4 -> OnboardingPage(
                            modifier = pagerModifier,
                            title = stringResource(R.string.onboarding_page5_title),
                            content = stringResource(R.string.onboarding_page5_content),
                            icon = Icons.Default.Public
                        )

                        5 -> OnboardingPage(
                            modifier = pagerModifier,
                            title = stringResource(R.string.onboarding_page6_title),
                            content = stringResource(R.string.onboarding_page6_content),
                            icon = Icons.Default.CheckCircle
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            if (pagerState.currentPage == 5) {
                                onClose()
                            } else {
                                scope.launch {
                                    val nextPage = (pagerState.currentPage + 1) % 6
                                    pagerState.animateScrollToPage(nextPage)
                                }
                            }

                        }
                    ) {
                        Text(if (pagerState.currentPage == 5) stringResource(R.string.got_it_thx) else stringResource(R.string.continue_))
                    }
                }

            }
        }
    }
}

@Composable
fun OnboardingPage(
    modifier: Modifier = Modifier,
    title: String, // White part
    content: String,
    icon: ImageVector?,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val title2 by remember {
            mutableStateOf(" " + (title
                .split(" ")
                .last()))
        }

        val title1 by remember {
            mutableStateOf(
                title.dropLast(title2.length)
            )
        }

        val accentColor = if (isSystemInDarkTheme()) greenAccentBlackTheme else greenAccentWhiteTheme

        icon?.let {
            Icon(
                modifier = Modifier
                    .size(60.dp),
                imageVector = icon,
                contentDescription = null,
                tint = accentColor.copy(alpha = 0.75f)
            )
        }

        Spacer(Modifier.size(20.dp))

        Text(
            text = title1.trim(),
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
        )

        Text(
            text = title2.trim(),
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Black,
            color = accentColor
        )

        Spacer(Modifier.size(20.dp))

        Text(
            text = content,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    SolocardsTheme {
        OnboardingScreen()
    }
}