package com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.props_dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R

@Composable
fun AccordionToggler(
    section: AccordionSection,
    activeSection: AccordionSection?,
    label: String,
    onClick: (AccordionSection?) -> Unit,
    helpMessage: String,
    content: (@Composable () -> Unit)
) {
    val context = LocalContext.current
    val isActive = section == activeSection

    Column {
        TextButton(
            onClick = {
                if (isActive) {
                    onClick(null)
                } else {
                    onClick(section)
                }
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${if (isActive) '▼' else '▶'}   $label",
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(Modifier.size(16.dp))

                if (isActive) {
                    IconButton(
                        modifier = Modifier
                            .size(32.dp) ,
                        onClick = {
                            Toast.makeText(context, helpMessage, Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                            contentDescription = stringResource(R.string.help),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }

        }

        if (activeSection == section) content()
    }
}