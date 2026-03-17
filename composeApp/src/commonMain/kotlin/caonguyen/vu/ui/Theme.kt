package caonguyen.vu.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val IoTDarkBackground = Color(0xFF1E1E2C)
val IoTSurface = Color(0xFF2B2B40)
val IoTPrimary = Color(0xFF00FFCC) // Neon Cyberpunk/IoT teal
val IoTSecondary = Color(0xFFFF007F) // Neon Pink for alerts
val IoTText = Color(0xFFE0E0E0)
val IoTTextSecondary = Color(0xFFA0A0B0)

val IoTDarkColors = darkColorScheme(
    background = IoTDarkBackground,
    surface = IoTSurface,
    surfaceVariant = IoTSurface,
    primary = IoTPrimary,
    secondary = IoTSecondary,
    onBackground = IoTText,
    onSurface = IoTText,
    onSurfaceVariant = IoTTextSecondary,
    onPrimary = Color.Black
)
