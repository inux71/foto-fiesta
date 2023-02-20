package com.xdteam.fotofiesta.presentation.pdf_screen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xdteam.fotofiesta.domain.model.SerieWithImages
import com.xdteam.fotofiesta.domain.repository.PDFRepository
import com.xdteam.fotofiesta.domain.repository.SerieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class PDFScreenState(
    val series: List<SerieWithImages> = emptyList()
)

@HiltViewModel
class PDFScreenViewModel @Inject constructor(
    private val _pdfRepository: PDFRepository,
    private val _serieRepository: SerieRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PDFScreenState())
    val state: StateFlow<PDFScreenState>
        get() = _state

    init {
        getSeries()
    }

    private fun getSeries() {
             _serieRepository.getSeries().onEach { series ->
                _state.value = _state.value.copy(series = series)
            }.launchIn(viewModelScope)

        Log.i("PDFScreenViewModel", "getSeries: ${_state.value.series}")
    }

    fun downloadPDF(filename: String, context: Context) = viewModelScope.launch {
        val path = _pdfRepository.downloadFile(filename)

        if (path != null) {
            val file = File(path, filename)
            val uri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                file
            )

            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(intent)

        } else {
            Toast.makeText(context, "Nie udało się otworzyć pliku!", Toast.LENGTH_SHORT).show()
        }
    }
}
