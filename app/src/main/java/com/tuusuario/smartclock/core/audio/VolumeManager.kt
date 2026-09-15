package com.tuusuario.smartclock.core.audio

import android.content.Context
import android.media.AudioManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VolumeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun getStreamVolume(streamType: Int = AudioManager.STREAM_MUSIC): Int {
        return audioManager.getStreamVolume(streamType)
    }

    fun getMaxStreamVolume(streamType: Int = AudioManager.STREAM_MUSIC): Int {
        return audioManager.getStreamMaxVolume(streamType)
    }

    fun setStreamVolume(volume: Int, streamType: Int = AudioManager.STREAM_MUSIC) {
        audioManager.setStreamVolume(streamType, volume, 0)
    }

    /**
     * Incrementa progresivamente el volumen desde el nivel actual hasta el objetivo (Efecto Fade-in).
     */
    suspend fun fadeInVolume(
        targetVolume: Int,
        durationMs: Long = 3000L,
        streamType: Int = AudioManager.STREAM_MUSIC
    ) {
        val maxVol = getMaxStreamVolume(streamType)
        val target = targetVolume.coerceIn(0, maxVol)
        val initialVol = getStreamVolume(streamType)
        
        if (initialVol >= target) return

        val steps = (target - initialVol).coerceAtLeast(1)
        val delayPerStep = durationMs / steps

        for (current in initialVol..target) {
            setStreamVolume(current, streamType)
            delay(delayPerStep)
        }
    }
}

