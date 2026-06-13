package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryStreamScreen(activeAgent: String, onOpenDrawer: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    
    Box(modifier = Modifier.fillMaxSize().background(VantaBlack)) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            TopAppBar(
                title = { Text("$activeAgent // MEMORY", style = MaterialTheme.typography.labelLarge, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            // Search Bar
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = GlassBackground,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Query memories...", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("TOTAL", "1,024")
                StatItem("ACTIVE", "3", NeonRed)
                StatItem("PAUSED", "12", WarningColor)
                StatItem("COMPLETED", "1,009", CyanIntel)
            }

            // Memory List
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { MemoryCard("Project Alpha Initialization", "Session 0A93F", "2 hrs ago", CyanIntel, listOf("sys", "init")) }
                item { MemoryCard("Data Harvesting Routine", "Session 1B44E", "5 hrs ago", NeonRed, listOf("net", "auto")) }
                item { MemoryCard("UI Synthesis Check", "Session 9C88D", "1 day ago", WarningColor, listOf("ui", "test")) }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, valueColor: Color = TextPrimary) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, color = valueColor)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}

@Composable
fun MemoryCard(title: String, subtitle: String, time: String, statusColor: Color, tags: List<String>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = GlassBackground,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Status bar on left
            Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(statusColor))
            
            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Box(modifier = Modifier.size(8.dp).background(statusColor, RoundedCornerShape(50)))
                }
                Spacer(Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        tags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AbyssBlack,
                                border = BorderStroke(1.dp, TextSecondary.copy(alpha = 0.3f))
                            ) {
                                Text(tag, style = MaterialTheme.typography.labelSmall, color = TextSecondary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                    Text(time, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
        }
    }
}
