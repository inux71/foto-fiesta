package com.xdteam.fotofiesta.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.xdteam.fotofiesta.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsPage() {
    var delayText by remember {
        mutableStateOf("1")
    }

    var seriePhotoSizeText by remember {
        mutableStateOf("5")
    }

    Column(
        Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(stringResource(R.string.delay_option_header))
            },
            value = delayText,
            onValueChange = {
                delayText = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(stringResource(R.string.serie_photo_size))
            },
            value = seriePhotoSizeText,
            onValueChange = {
                seriePhotoSizeText = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
