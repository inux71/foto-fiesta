package com.xdteam.fotofiesta.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xdteam.fotofiesta.R

@Composable
fun SettingsPage() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 0.dp)
    ) {
        SettingsOption(
            header = stringResource(R.string.delay_option_header),
            placeholder = "1"
        )
        SettingsOption(
            header = stringResource(R.string.serie_photo_size),
            placeholder = "5"
        )
    }
}
