package com.tuusuario.nexustime.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.nexustime.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen() {
    var isRightPanelVisible by remember { mutableStateOf(false) }
    var isAuraActive by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackOLED)
    ) {
        // Fondo / Animación Aura
        if (isAuraActive) {
            AuraBackground()
        }

        Column(modifier = Modifier.fillMaxSize()) {
            
            // Área Principal: Reloj (Izquierda) y Panel (Derecha)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lado Izquierdo: Reloj
                ClockWidget(
                    modifier = Modifier.weight(1f),
                    onTitleClick = { isAuraActive = !isAuraActive }
                )

                // Lado Derecho: Panel Retráctil
                AnimatedVisibility(
                    visible = isRightPanelVisible,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    RightPanel(modifier = Modifier.width(340.dp))
                }
            }

            // Barra Inferior de Navegación
            BottomNavBar(
                onCalendarClick = { isRightPanelVisible = !isRightPanelVisible }
            )
        }
    }
}

@Composable
fun AuraBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "AuraAnimation")
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AuraAlpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ElectricBlue.copy(alpha = auraAlpha),
                    SkyBlue.copy(alpha = auraAlpha * 0.5f),
                    androidx.compose.ui.graphics.Color.Transparent
                ),
                center = Offset(size.width * 0.3f, size.height * 0.5f), // Centrado detrás del reloj
                radius = size.height * 0.8f
            ),
            center = Offset(size.width * 0.3f, size.height * 0.5f),
            radius = size.height * 0.8f
        )
    }
}

@Composable
fun ClockWidget(modifier: Modifier = Modifier, onTitleClick: () -> Unit) {
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        while (true) {
            val now = Date()
            currentTime = timeFormat.format(now)
            currentDate = dateFormat.format(now).uppercase()
            delay(1000L)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título Clickable para activar/desactivar Aura
        Text(
            text = "NEXUSTIME",
            color = SilverMetal,
            fontSize = 16.sp,
            letterSpacing = 2.sp,
            modifier = Modifier
                .clickable { onTitleClick() }
                .padding(bottom = 16.dp)
        )

        Text(
            text = currentTime.ifEmpty { "00:00" },
            fontSize = 130.sp,
            fontWeight = FontWeight.Bold,
            color = WhitePure,
            letterSpacing = (-4).sp
        )
        Text(
            text = currentDate,
            fontSize = 24.sp,
            color = SilverMetal,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "LIMA | EST 22:44", // Ubicación estática por ahora
            fontSize = 18.sp,
            color = SkyBlue,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun BottomNavBar(onCalendarClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO */ }, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(Icons.Default.Schedule, contentDescription = "Reloj", tint = SilverMetal)
        }
        IconButton(onClick = onCalendarClick, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(Icons.Default.CalendarMonth, contentDescription = "Calendario", tint = ElectricBlue)
        }
        IconButton(onClick = { /* TODO */ }, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(Icons.Default.Alarm, contentDescription = "Alarmas", tint = SilverMetal)
        }
        IconButton(onClick = { /* TODO */ }, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = SilverMetal)
        }
    }
}

