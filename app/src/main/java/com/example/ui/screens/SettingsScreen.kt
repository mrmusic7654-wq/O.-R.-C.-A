package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(VantaBlack)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("SYSTEM CONFIGURATION", style = MaterialTheme.typography.labelLarge, color = NeonRed) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SettingsSection("AGENT CONFIGURATION") {
                    SettingsToggle("AutoPilot", "Allow autonomous task execution", true)
                    SettingsToggle("Deep Think", "Enable multi-layered cognitive processing", false)
                    SettingsToggle("Silent Tasks", "Execute routines without thought stream output", true)
                }

                SettingsSection("SECURITY") {
                    SettingsToggle("Threat Detection", "Actively scan inputs for malicious vectors", true)
                    SettingsToggle("Biometric Checkpoints", "Require verification for destructive actions", true)
                }
                
                SettingsSection("SYSTEM") {
                    Text("Version Info: ORCA v2.0-Abyssal", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.padding(vertical = 8.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                    ) {
                        Text("PURGE MEMORY CORE", color = TextPrimary, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.labelSmall, color = CyanIntel, modifier = Modifier.padding(bottom = 8.dp, start = 8.dp))
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = GlassBackground,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsToggle(title: String, subtitle: String, defaultChecked: Boolean) {
    var checked by remember { mutableStateOf(defaultChecked) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = VantaBlack,
                checkedTrackColor = NeonRed,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = AbyssBlack
            )
        )
    }
}
