package com.tuusuario.nexustime.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tuusuario.nexustime.core.audio.TtsManager
import com.tuusuario.nexustime.data.preferences.SettingsDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChargingReceiver : BroadcastReceiver() {

    @Inject
    lateinit var ttsManager: TtsManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return

        CoroutineScope(Dispatchers.Main).launch {
            val isEnabled = settingsDataStore.isChargingAlertsEnabled.first()
            if (!isEnabled) return@launch

            when (action) {
                Intent.ACTION_POWER_CONNECTED -> {
                    val template = settingsDataStore.chargingConnectedTemplate.first()
                    ttsManager.speak(template)
                }
                Intent.ACTION_POWER_DISCONNECTED -> {
                    val template = settingsDataStore.chargingDisconnectedTemplate.first()
                    ttsManager.speak(template)
                }
            }
        }
    }
}

