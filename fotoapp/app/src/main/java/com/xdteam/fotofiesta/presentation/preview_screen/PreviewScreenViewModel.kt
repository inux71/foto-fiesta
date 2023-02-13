package com.xdteam.fotofiesta.presentation.preview_screen

import android.os.CountDownTimer
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.round

enum class TIMER_STATE {
    IDLE,
    STARTED,
}

data class PreviewScreenViewState(
    val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    val timerState: TIMER_STATE = TIMER_STATE.IDLE,
    val countDownTime: Long = 0,
    val picturesTaken: Int = 0,
)

@HiltViewModel
class PreviewScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = mutableStateOf(PreviewScreenViewState())
    val state: State<PreviewScreenViewState> = _state

    private var timer: CountDownTimer? = null

    private var _onSinglePhotoDone: (() -> Unit)? = null
    private var _onSerieFinished: (() -> Unit)? = null
    private var _onDelayReached: (() -> Unit)? = null

    fun setCameraSelector(cameraSelector: CameraSelector) {
        _state.value = _state.value.copy(cameraSelector = cameraSelector)
    }

    fun startSeries(
        onSinglePhotoDone: () -> Unit,
        onSerieFinished: () -> Unit,
        onDelayReached: () -> Unit
    ) {
        _onSinglePhotoDone = onSinglePhotoDone
        _onSerieFinished = onSerieFinished
        _onDelayReached = onDelayReached

        _state.value = _state.value.copy(timerState = TIMER_STATE.STARTED, countDownTime = 10, picturesTaken = 0)

        restartTimer()
    }

    fun takePicture() {
        _state.value = _state.value.copy(picturesTaken = _state.value.picturesTaken + 1)

        if (_state.value.picturesTaken == 5) {
            _state.value = _state.value.copy(timerState = TIMER_STATE.IDLE, picturesTaken = 0)
            timer?.cancel()
            _onSerieFinished?.let { it() }
        } else {
            restartTimer()
            _onSinglePhotoDone?.let { it() }
        }
    }

    private fun restartTimer() {
        _state.value = _state.value.copy(countDownTime = 10)

        timer?.cancel()

        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _state.value = _state.value.copy(countDownTime = round(millisUntilFinished / 1000f).toLong())
            }

            override fun onFinish() {
                takePicture()
            }
        }.start()
    }

    fun stopSeries() {
        timer?.cancel()

        _state.value = _state.value.copy(timerState = TIMER_STATE.IDLE)
    }
}
