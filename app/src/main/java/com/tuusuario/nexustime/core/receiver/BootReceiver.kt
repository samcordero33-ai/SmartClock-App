package com.tuusuario.nexustime.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tuusuario.nexustime.core.alarm.ChimeScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var chimeScheduler: ChimeScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            chimeScheduler.scheduleHourlyChime()
        }
    }
}

