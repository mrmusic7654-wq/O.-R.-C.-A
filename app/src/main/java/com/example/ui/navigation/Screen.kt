package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.filled.Outbound
import androidx.compose.material.icons.automirrored.outlined.Outbound
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.TripOrigin
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector
) {
    object Orb : Screen("orb", "ORB", Icons.Filled.TripOrigin, Icons.Outlined.TripOrigin)
    object Chat : Screen("chat", "CHAT", Icons.AutoMirrored.Filled.Chat, Icons.AutoMirrored.Outlined.Chat)
    object Memory : Screen("memory", "MEMORY", Icons.Filled.Memory, Icons.Outlined.Memory)
    object WarRoom : Screen("war_room", "WAR ROOM", Icons.AutoMirrored.Filled.Outbound, Icons.AutoMirrored.Outlined.Outbound)
    object Metrics : Screen("metrics", "METRICS", Icons.Filled.Analytics, Icons.Outlined.Analytics)
    object Settings : Screen("settings", "SETTINGS", Icons.Filled.Settings, Icons.Filled.Settings)
}

val BottomNavItems = listOf(
    Screen.Orb,
    Screen.Chat,
    Screen.Memory,
    Screen.WarRoom,
    Screen.Metrics
)
