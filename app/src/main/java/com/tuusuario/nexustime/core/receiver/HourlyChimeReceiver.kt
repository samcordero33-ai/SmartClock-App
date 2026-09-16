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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HourlyChimeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var ttsManager: TtsManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.Main).launch {
            val isEnabled = settingsDataStore.isChimeEnabled.first()
            if (!isEnabled) return@launch

            val template = settingsDataStore.chimeTemplate.first()
            val sdf = SimpleDateFormat("h a", Locale.getDefault())
            val currentHourStr = sdf.format(Date())

            ttsManager.speakTemplate(template, mapOf("HORA" to currentHourStr))
        }
    }
}

