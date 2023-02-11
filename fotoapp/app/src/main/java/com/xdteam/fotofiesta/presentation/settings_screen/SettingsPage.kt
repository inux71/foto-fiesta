package com.xdteam.fotofiesta.presentation.settings_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xdteam.fotofiesta.R
import com.xdteam.fotofiesta.presentation.SettingsOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settings_page_header), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(10.dp)
        ) {
            SettingsOption(
                optionsList = (1..10).toList(),
                label = stringResource(R.string.delay_option_header)
            )
            SettingsOption(
                optionsList = (2..5).toList(),
                label = stringResource(R.string.serie_photo_size)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_button),
                )
            }
        }
    }

}
