package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricsScreen(activeAgent: String, onOpenDrawer: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(VantaBlack)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("$activeAgent // METRICS", style = MaterialTheme.typography.labelLarge, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) { Icon(Icons.Default.Menu, "Menu", tint = TextPrimary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = GlassBackground,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("SYSTEM TELEMETRY", style = MaterialTheme.typography.labelMedium, color = CyanIntel)
                            Spacer(Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Column {
                                    Text("34,021", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                                    Text("TK USAGE", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                                Column {
                                    Text("1,402", style = MaterialTheme.typography.headlineMedium, color = NeonRed)
                                    Text("API REQ", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                            LinearProgressIndicator(progress = { 0.7f }, modifier = Modifier.fillMaxWidth(), color = CyanIntel, trackColor = AbyssBlack)
                            Text("COMPUTE LOAD: 70%", style = MaterialTheme.typography.labelSmall, color = TextSecondary, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }

                item {
                    Text("ACTIVE AGENT POOLS", style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                }

                items(listOf("Network Harvesting (Auto)", "UI Rendering Loop", "Cognitive Deep Think", "API Synchronisation Segment")) { task ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        color = AbyssBlack,
                        border = BorderStroke(1.dp, NeonRed.copy(alpha = 0.3f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("> $task", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Text("RUNNING", style = MaterialTheme.typography.labelSmall, color = NeonRedPulse)
                        }
                    }
                }
            }
        }
    }
}
