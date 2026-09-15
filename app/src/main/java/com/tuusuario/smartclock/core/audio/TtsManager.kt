package com.tuusuario.smartclock.core.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "ES"))
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isInitialized = true
            } else {
                // Intento alternativo con español latino o idioma por defecto del sistema
                tts?.language = Locale.getDefault()
                isInitialized = true
            }
        }
    }

    /**
     * Reproduce un texto directo por voz.
     */
    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (isInitialized) {
            tts?.speak(text, queueMode, null, "SmartClockTTS_" + System.currentTimeMillis())
        }
    }

    /**
     * Procesa una plantilla con variables y la reproduce.
     * Ejemplo: plantilla = "Son las {HORA}" -> procesado = "Son las 3:00 PM"
     */
    fun speakTemplate(template: String, replacements: Map<String, String>) {
        var processedText = template
        replacements.forEach { (key, value) ->
            processedText = processedText.replace("{$key}", value)
        }
        speak(processedText)
    }

    fun stop() {
        if (isInitialized) {
            tts?.stop()
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}

