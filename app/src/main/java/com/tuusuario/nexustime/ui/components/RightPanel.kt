package com.tuusuario.nexustime.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.nexustime.ui.theme.*

@Composable
fun RightPanel(
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

        // Tarjetas de Eventos (Agenda)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EventCard(
                time = "11:30 AM",
                title = "Reunión de Proyecto",
                location = "Google Meet",
                accentColor = ElectricBlue
            )
            EventCard(
                time = "01:00 PM",
                title = "Almuerzo de Trabajo",
                location = "Café Central",
                accentColor = SkyBlue
            )
            EventCard(
                time = "03:45 PM",
                title = "Revisión de Avances",
                location = "Sala B",
                accentColor = SilverMetal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Switches / Alarmas Rápidas
        Text(
            text = "CONTROLES RÁPIDOS",
            color = SilverMetal,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        var alarm1Enabled by remember { mutableStateOf(true) }
        var alarm2Enabled by remember { mutableStateOf(false) }

        AlarmToggleRow(
            label = "Alarma 07:00 AM",
            checked = alarm1Enabled,
            onCheckedChange = { alarm1Enabled = it }
        )
        AlarmToggleRow(
            label = "Alarma 08:30 AM",
            checked = alarm2Enabled,
            onCheckedChange = { alarm2Enabled = it }
        )
    }
}

@Composable
fun EventCard(
    time: String,
    title: String,
    location: String,
    accentColor: androidx.compose.ui.graphics.Color
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

