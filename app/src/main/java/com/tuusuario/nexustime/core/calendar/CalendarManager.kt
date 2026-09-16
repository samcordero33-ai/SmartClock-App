package com.tuusuario.nexustime.core.calendar

import android.content.ContentUris
import android.content.Context
import android.provider.CalendarContract
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

data class CalendarEvent(
    val title: String,
    val startTime: String
)

@Singleton
class CalendarManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getTodayEvents(): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()
        
        val startTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        val endTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        val projection = arrayOf(
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.BEGIN
        )

        val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startTime)
        ContentUris.appendId(builder, endTime)

        try {
            val cursor = context.contentResolver.query(
                builder.build(),
                projection,
                null,
                null,
                "${CalendarContract.Instances.BEGIN} ASC"
            )

            cursor?.use {
                val titleIndex = it.getColumnIndex(CalendarContract.Instances.TITLE)
                val beginIndex = it.getColumnIndex(CalendarContract.Instances.BEGIN)

                while (it.moveToNext()) {
                    val title = if (titleIndex != -1) it.getString(titleIndex) else "Evento"
                    val begin = if (beginIndex != -1) it.getLong(beginIndex) else 0L
                    
                    val cal = Calendar.getInstance().apply { timeInMillis = begin }
                    val hour = cal.get(Calendar.HOUR_OF_DAY)
                    val minute = String.format("%02d", cal.get(Calendar.MINUTE))
                    val timeStr = "$hour:$minute"

                    events.add(CalendarEvent(title, timeStr))
                }
            }
        } catch (e: SecurityException) {
            // Manejo si el permiso de lectura no ha sido concedido en tiempo de ejecución
        }

        return events
    }
}

