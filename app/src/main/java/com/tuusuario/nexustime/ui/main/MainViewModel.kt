package com.tuusuario.nexustime.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.nexustime.core.audio.TtsManager
import com.tuusuario.nexustime.core.calendar.CalendarEvent
import com.tuusuario.nexustime.core.calendar.CalendarManager
import com.tuusuario.nexustime.data.preferences.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class DashboardUiState(
    val events: List<CalendarEvent> = emptyList(),
    val isAuraActive: Boolean = false,
    val isRightPanelVisible: Boolean = false,
    val isChimeEnabled: Boolean = true,
    val isChargingAlertsEnabled: Boolean = true,
    val weatherSummary: String = "21°C | Despejado"
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val calendarManager: CalendarManager,
    private val settingsDataStore: SettingsDataStore,
    private val ttsManager: TtsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadSettingsAndEvents()
    }

    fun loadSettingsAndEvents() {
        viewModelScope.launch {
            val chime = settingsDataStore.isChimeEnabled.first()
            val charging = settingsDataStore.isChargingAlertsEnabled.first()
            val todayEvents = calendarManager.getTodayEvents()

            _uiState.update { currentState ->
                currentState.copy(
                    events = todayEvents,
                    isChimeEnabled = chime,
                    isChargingAlertsEnabled = charging
                )
            }
        }
    }

    fun toggleAura() {
        _uiState.update { it.copy(isAuraActive = !it.isAuraActive) }
    }

    fun toggleRightPanel() {
        _uiState.update { it.copy(isRightPanelVisible = !it.isRightPanelVisible) }
    }

    fun toggleChime(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setChimeEnabled(enabled)
            _uiState.update { it.copy(isChimeEnabled = enabled) }
        }
    }

    fun toggleChargingAlerts(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setChargingAlertsEnabled(enabled)
            _uiState.update { it.copy(isChargingAlertsEnabled = enabled) }
        }
    }

    fun readDailyBriefing() {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("EEEE d 'de' MMMM", Locale.getDefault()).format(Date())
            val eventCount = _uiState.value.events.size

            val text = if (eventCount > 0) {
                "Hoy es $dateStr. Tienes $eventCount eventos programados en tu agenda. El clima actual es ${_uiState.value.weatherSummary}."
            } else {
                "Hoy es $dateStr. No tienes eventos pendientes para hoy. El clima es ${_uiState.value.weatherSummary}."
            }

            ttsManager.speak(text)
        }
    }
}

