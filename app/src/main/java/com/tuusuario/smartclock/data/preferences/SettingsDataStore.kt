package com.tuusuario.smartclock.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "smart_clock_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    private val context: Context
) {
    companion object {
        // Toggles / Casillas de activación
        val CHIME_ENABLED = booleanPreferencesKey("chime_enabled")
        val RELATIVE_CHIME_ENABLED = booleanPreferencesKey("relative_chime_enabled")
        val CHARGING_ALERTS_ENABLED = booleanPreferencesKey("charging_alerts_enabled")
        val WEATHER_REPORT_ENABLED = booleanPreferencesKey("weather_report_enabled")
        val DYNAMIC_VOLUME_ENABLED = booleanPreferencesKey("dynamic_volume_enabled")

        // Plantillas de texto editables por el usuario
        val CHIME_TEMPLATE = stringPreferencesKey("chime_template")
        val CHARGING_CONNECTED_TEMPLATE = stringPreferencesKey("charging_connected_template")
        val CHARGING_DISCONNECTED_TEMPLATE = stringPreferencesKey("charging_disconnected_template")
    }

    // --- Lectura de Toggles ---
    val isChimeEnabled: Flow<Boolean> = context.dataStore.data.map { it[CHIME_ENABLED] ?: true }
    val isRelativeChimeEnabled: Flow<Boolean> = context.dataStore.data.map { it[RELATIVE_CHIME_ENABLED] ?: false }
    val isChargingAlertsEnabled: Flow<Boolean> = context.dataStore.data.map { it[CHARGING_ALERTS_ENABLED] ?: true }
    val isWeatherReportEnabled: Flow<Boolean> = context.dataStore.data.map { it[WEATHER_REPORT_ENABLED] ?: true }
    val isDynamicVolumeEnabled: Flow<Boolean> = context.dataStore.data.map { it[DYNAMIC_VOLUME_ENABLED] ?: false }

    // --- Lectura de Plantillas de Voz (Valores por defecto) ---
    val chimeTemplate: Flow<String> = context.dataStore.data.map { 
        it[CHIME_TEMPLATE] ?: "Son las {HORA}" 
    }
    val chargingConnectedTemplate: Flow<String> = context.dataStore.data.map { 
        it[CHARGING_CONNECTED_TEMPLATE] ?: "Cargador conectado" 
    }
    val chargingDisconnectedTemplate: Flow<String> = context.dataStore.data.map { 
        it[CHARGING_DISCONNECTED_TEMPLATE] ?: "Desconectado" 
    }

    // --- Guardar Cambios ---
    async fun setToggle(key: androidx.datastore.preferences.core.Preferences.Key<Boolean>, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[key] = enabled
        }
    }

    suspend fun setTemplate(key: androidx.datastore.preferences.core.Preferences.Key<String>, template: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = template
        }
    }
}

