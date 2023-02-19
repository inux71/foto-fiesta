package com.xdteam.fotofiesta.presentation.preview_screen

import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xdteam.fotofiesta.domain.model.Image
import com.xdteam.fotofiesta.domain.model.Serie
import com.xdteam.fotofiesta.domain.model.SerieWithImages
import com.xdteam.fotofiesta.domain.repository.ImageRepository
import com.xdteam.fotofiesta.domain.repository.PDFRepository
import com.xdteam.fotofiesta.domain.repository.SerieRepository
import com.xdteam.fotofiesta.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

enum class TimerState {
    IDLE,
    STARTED,
}

@HiltViewModel
class PreviewScreenViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val _serieRepository: SerieRepository,
    private val _imageRepository: ImageRepository,
    private val _pdfRepository: PDFRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PreviewScreenState())
    val state: StateFlow<PreviewScreenState>
        get() = _state

    private val _countdownFlow = MutableSharedFlow<PreviewScreenEvent>()
    val countdownFlow = _countdownFlow.asSharedFlow()

    private var countdownJob: Job? = null
    private var timer: CountDownTimer? = null

    init {
        viewModelScope.launch {
            val (delay, numberOfPhotos) = syncSettings()

            _state.value = _state.value.copy(photoDelay = delay, numberOfPhotos = numberOfPhotos)
        }
    }

    fun startTimer() {
        _state.value = _state.value.copy(timerState = TimerState.STARTED)
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch {
            val (delay, numberOfPhotos) = syncSettings()
            _state.value = _state.value.copy(photoDelay = delay, numberOfPhotos = numberOfPhotos, picturesTaken = 0)

            _countdownFlow.emit(PreviewScreenEvent.SeriesStarted)

            countdownTimer(delay.toLong(), TimeUnit.SECONDS, numberOfPhotos, 100).collect {
                when(it) {
                    is TimerEvent.OnTick -> {
                        _state.value = _state.value.copy(timerValue = (it.remaining / 1000.0))
                    }
                    // NOTE: Series is actually one photo XDD
                    TimerEvent.SeriesFinished -> {
                        _countdownFlow.emit(PreviewScreenEvent.SeriesFinished)
                        _state.value = _state.value.copy(picturesTaken = _state.value.picturesTaken + 1)
                    }
                    TimerEvent.OnFinished -> {
                        Log.i("[ff]", "Finished!")
                        _state.value = _state.value.copy(timerState = TimerState.IDLE)

                        val serie = Serie(null)
                        val serieId = _serieRepository.insertSerie(serie)

                        _state.value.currentPictures.forEach { imagePath ->
                            _imageRepository.insert(Image(null, serieId, imagePath))
                        }

                        val serieWithImages = _serieRepository.getSerieById(serieId)
                        Log.i("SerieWithImages", serieWithImages.toString())

                        val files: List<File> = serieWithImages.images.map { image ->
                            File(image.path)
                        }

                        _pdfRepository.uploadFiles(files, serieId.toString())
                    }
                }
            }
        }
    }

    fun cancelSeries() {
        _state.value = _state.value.copy(timerState = TimerState.IDLE)
        countdownJob?.cancel()
    }

    fun flipCamera() {
        val lensFacing = if (_state.value.lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        _state.value = _state.value.copy(
            lensFacing = lensFacing,
            cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        )
    }

    fun addPicture(path: String) {
        val newPictures = _state.value.currentPictures.toMutableList()
        newPictures.add(path)
        _state.value = _state.value.copy(currentPictures = newPictures)

        Log.i("[ff]", "Added picture: $path")
    }

    private suspend fun syncSettings(): Settings {
        val delay = settingsRepository.getDelay().getOrNull() ?: 1
        val numberOfPhotos = settingsRepository.getNumberOfPhotos().getOrNull() ?: 2

        return Settings(delay, numberOfPhotos)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}

data class PreviewScreenState(
    val photoDelay: Int = 0,
    val numberOfPhotos: Int = 0,
    val timerState: TimerState = TimerState.IDLE,
    val timerValue: Double = 0.0,
    val picturesTaken: Int = 0,
    val currentPictures: MutableList<String> = mutableListOf(),
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    val cameraSelector: CameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
)

private fun countdownTimer(duration: Long, timeUnit: TimeUnit, restartCount: Int, delayMillis: Long = 1000L) = flow {
    var timeRemaining = timeUnit.toMillis(duration)
    var restartsRemaining = restartCount
    while (restartsRemaining > 0) {
        emit(TimerEvent.OnTick(timeRemaining))
        delay(delayMillis)
        timeRemaining -= delayMillis
        if (timeRemaining <= 0) {
            emit(TimerEvent.SeriesFinished)

            // Photo grace period
            delay(500)

            restartsRemaining--
            if (restartsRemaining > 0) {
                timeRemaining = timeUnit.toMillis(duration)
            } else {
                emit(TimerEvent.OnFinished)
            }
        }
    }
}

sealed class PreviewScreenEvent {
    object SeriesStarted : PreviewScreenEvent()
    object SeriesFinished : PreviewScreenEvent()
}

sealed class TimerEvent {
    class OnTick(val remaining: Long) : TimerEvent()
    object SeriesFinished : TimerEvent()
    object OnFinished : TimerEvent()
}

data class Settings(
    val photoDelay: Int,
    val numberOfPhotos: Int
)