package com.xdteam.fotofiesta.presentation.settings_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xdteam.fotofiesta.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val delay: Int = 0,
    val numberOfPhotos: Int = 0
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    init {
        initState()
    }

    fun onDelayChanged(delay: Int) {
        viewModelScope.launch {
            settingsRepository.setDelay(delay)
            _state.value = _state.value.copy(delay = delay)
        }
    }

    fun onNumberOfPhotosChanged(numberOfPhotos: Int) {
        viewModelScope.launch {
            settingsRepository.setNumberOfPhotos(numberOfPhotos)
            _state.value = _state.value.copy(numberOfPhotos = numberOfPhotos)
        }
    }

    private fun initState() {
        viewModelScope.launch {
            val delay = settingsRepository.getDelay().getOrNull() ?: 0
            val numberOfPhotos = settingsRepository.getNumberOfPhotos().getOrNull() ?: 0

            _state.value = SettingsState(delay, numberOfPhotos)
        }
    }
}