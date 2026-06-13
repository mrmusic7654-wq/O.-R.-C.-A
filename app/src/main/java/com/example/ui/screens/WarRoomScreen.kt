package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarRoomScreen(activeAgent: String, onOpenDrawer: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(VantaBlack)) {
        // Topological map background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            
            // Draw orbits
            drawCircle(color = TextSecondary.copy(alpha = 0.1f), radius = size.width * 0.3f, center = center, style = Stroke(width = 1f))
            drawCircle(color = TextSecondary.copy(alpha = 0.1f), radius = size.width * 0.6f, center = center, style = Stroke(width = 1f))
            
            // Central Goal
            drawCircle(color = NeonRed, radius = 30f, center = center)
            drawCircle(color = NeonRedPulse.copy(alpha = 0.3f), radius = 50f, center = center)
            
            // Nodes
            // Completed node
            val node1 = Offset(center.x + size.width * 0.3f * Math.cos(0.5).toFloat(), center.y + size.width * 0.3f * Math.sin(0.5).toFloat())
            drawLine(color = CyanIntel.copy(alpha = 0.5f), start = center, end = node1, strokeWidth = 2f)
            drawCircle(color = CyanIntel, radius = 15f, center = node1)
            
            // Active node
            val node2 = Offset(center.x - size.width * 0.6f * Math.cos(0.8).toFloat(), center.y - size.width * 0.6f * Math.sin(0.8).toFloat())
            drawLine(color = NeonRedPulse.copy(alpha = 0.5f), start = center, end = node2, strokeWidth = 4f)
            drawCircle(color = NeonRedPulse, radius = 20f, center = node2)
        }
        
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("$activeAgent // TASK CHAIN", style = MaterialTheme.typography.labelLarge, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) { Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
            
            Spacer(Modifier.weight(1f))
            
            // Stats & Panel
            Surface(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = AbyssBlack.copy(alpha = 0.9f),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CURRENT DIRECTIVE", style = MaterialTheme.typography.labelMedium, color = NeonRed)
                    Text("Execute global UI reskin and deploy to mainframe.", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                    Spacer(Modifier.height(16.dp))
                    LinearProgressIndicator(progress = { 0.6f }, modifier = Modifier.fillMaxWidth(), color = CyanIntel, trackColor = VantaBlack)
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("3 Nodes Complete", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text("2 Nodes Pending", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
            }
        }
    }
}
