package com.rombsquare.solocards.features.game_feature.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme

@Composable
fun YesNoButtons(
    modifier: Modifier = Modifier,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row {
            Button(
                modifier = Modifier
                    .height(60.dp)
                    .width(120.dp),
                onClick = onYesClick,
                shape = RoundedCornerShape(10),
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.yes),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(Modifier.width(8.dp))

            Button(
                modifier = Modifier
                    .height(60.dp)
                    .width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                onClick = onNoClick,
                shape = RoundedCornerShape(10),
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.no),
                    tint = MaterialTheme.colorScheme.onError,
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    SolocardsTheme {
        YesNoButtons(
            Modifier,
            {},
            {}
        )
    }
}