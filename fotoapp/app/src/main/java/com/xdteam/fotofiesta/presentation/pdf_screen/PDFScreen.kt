package com.xdteam.fotofiesta.presentation.pdf_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xdteam.fotofiesta.R
import com.xdteam.fotofiesta.domain.model.SerieWithImages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PDFScreen(viewModel: PDFScreenViewModel = hiltViewModel()) {
    val series: List<SerieWithImages> = emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.pdf_screen_header))
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(10.dp)
        ) {
            items(series) { serie ->
                PDFItem(serie.serie)
            }
        }
    }
}
