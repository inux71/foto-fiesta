package com.xdteam.fotofiesta.presentation.pdf_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.xdteam.fotofiesta.domain.model.SerieWithImages
import com.xdteam.fotofiesta.domain.repository.PDFRepository
import com.xdteam.fotofiesta.domain.repository.SerieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PDFScreenState(
    var series: List<SerieWithImages> = emptyList()
)

@HiltViewModel
class PDFScreenViewModel @Inject constructor(
    private val _pdfRepository: PDFRepository,
    private val _serieRepository: SerieRepository
) : ViewModel() {
    private var _state = mutableStateOf(PDFScreenState())
    val state: State<PDFScreenState> = _state

    init {
        getSeries()
    }

    fun getSeries() {
        viewModelScope.launch {
             _serieRepository.getSeries().onEach {
                 _state.value = state.value.copy(series = it)
             }
        }
    }

    fun downloadPDF(filename: String) = viewModelScope.launch {
        _pdfRepository.downloadFile(filename)
    }
}
