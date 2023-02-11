package com.xdteam.fotofiesta.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xdteam.fotofiesta.R

@Composable
fun SettingsPage() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 0.dp)
    ) {
        Text(
            stringResource(R.string.settings_page_header),
            fontSize = 22.sp,
            modifier = Modifier.padding(10.dp)
        )

        SettingsOption(
            optionsList = (1..10).toList(),
            label = stringResource(R.string.delay_option_header)
        )
        SettingsOption(
            optionsList = (2..5).toList(),
            label = stringResource(R.string.serie_photo_size)
        )
    }
}
