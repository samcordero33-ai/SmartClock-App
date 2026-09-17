package com.tuusuario.nexustime.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.nexustime.core.calendar.CalendarEvent
import com.tuusuario.nexustime.ui.theme.*

@Composable
fun RightPanel(
    events: List<CalendarEvent>,
    isChimeEnabled: Boolean,
    isChargingAlertsEnabled: Boolean,
    onChimeToggle: (Boolean) -> Unit,
    onChargingAlertsToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Cabecera del Panel
        Text(
            text = "AGENDA & ALARMAS",
            color = SkyBlue,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Lista de Eventos Reales de la Agenda
        if (events.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay eventos pendientes para hoy",
                    color = SilverMetal,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(events) { event ->
                    EventCard(
                        time = event.startTime,
                        title = event.title,
                        location = "Calendario Android",
                        accentColor = ElectricBlue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Controles Reales
        Text(
            text = "CONTROLES RÁPIDOS",
            color = SilverMetal,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        AlarmToggleRow(
            label = "Anuncio Horario (Chime)",
            checked = isChimeEnabled,
            onCheckedChange = onChimeToggle
        )
        AlarmToggleRow(
            label = "Alertas de Carga de Batería",
            checked = isChargingAlertsEnabled,
            onCheckedChange = onChargingAlertsToggle
        )
    }
}

@Composable
fun EventCard(
    time: String,
    title: String,
    location: String,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "$time | $title",
                    color = WhitePure,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = location,
                    color = SilverMetal,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun AlarmToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = WhitePure,
            fontSize = 13.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = WhitePure,
                checkedTrackColor = ElectricBlue,
                uncheckedThumbColor = SilverMetal,
                uncheckedTrackColor = DarkSurface
            )
        )
    }
}
