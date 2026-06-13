package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

enum class OrbState {
    IDLE, THINKING, EXECUTING, AUTONOMOUS, AWAITING_INPUT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrbScreen(
    activeAgent: String,
    onOpenDrawer: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var orbState by remember { mutableStateOf(OrbState.IDLE) }
    var autoPilotActive by remember { mutableStateOf(false) }
    var thoughtStreamVisible by remember { mutableStateOf(false) }
    
    val thoughts = remember { mutableStateListOf("> System initialized", "> Core routines stable") }
    
    LaunchedEffect(orbState) {
        if (orbState == OrbState.THINKING) {
            delay(2000)
            orbState = OrbState.EXECUTING
            delay(3000)
            orbState = OrbState.AWAITING_INPUT
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(VantaBlack)) {
        // Particle Background
        ParticleBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = {
                    Column {
                        Text(activeAgent, style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
                        Text("ABYSSAL NEON", style = MaterialTheme.typography.labelSmall, color = NeonRed)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("AUTOPILOT", style = MaterialTheme.typography.labelSmall, color = if (autoPilotActive) CyanIntel else TextSecondary)
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = autoPilotActive,
                            onCheckedChange = { 
                                autoPilotActive = it
                                orbState = if (it) OrbState.AUTONOMOUS else OrbState.IDLE
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = VantaBlack,
                                checkedTrackColor = CyanIntel,
                                uncheckedThumbColor = TextSecondary,
                                uncheckedTrackColor = AbyssBlack
                            ),
                            modifier = Modifier.scale(0.8f)
                        )
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            // Orb Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AbyssalOrb(orbState)
            }

            // Task Progress Placeholder
            if (orbState == OrbState.EXECUTING || orbState == OrbState.THINKING) {
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Compiling Sub-routine...", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().height(2.dp),
                        color = NeonRed,
                        trackColor = AbyssBlack
                    )
                }
            }

            // Command Deck
            CommandDeck(
                onSend = { text -> 
                     if (text.isNotEmpty()) {
                         thoughts.add("> User: $text")
                         thoughts.add("> Analyzing input syntax...")
                         orbState = OrbState.THINKING
                     }
                },
                onThoughtStreamToggle = { thoughtStreamVisible = !thoughtStreamVisible }
            )
            
            // Thought Stream Overlay Placeholder
            AnimatedVisibility(
                visible = thoughtStreamVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                ThoughtStreamPanel(thoughts)
            }
        }
    }
}

@Composable
fun ParticleBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "particle_alpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Simplified particle background representation
        for (i in 0 until 50) {
            val x = (Math.random() * size.width).toFloat()
            val y = (Math.random() * size.height).toFloat()
            drawCircle(
                color = CyanIntel.copy(alpha = alpha * (Math.random().toFloat())),
                radius = (Math.random() * 3).toFloat(),
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun AbyssalOrb(state: OrbState) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    
    // Scale animation
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (state == OrbState.AUTONOMOUS) 1.15f else 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (state == OrbState.EXECUTING) 400 else 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "orb_scale"
    )

    // Rotation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (state == OrbState.THINKING) 1000 else 8000, 
                easing = LinearEasing
            )
        ), label = "orb_rotation"
    )

    val primaryColor = when (state) {
        OrbState.THINKING -> CyanIntel
        OrbState.AUTONOMOUS -> NeonRedPulse
        else -> NeonRed
    }

    Box(
        modifier = Modifier
            .size(240.dp)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasCenter = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.width / 2f) * scale

            // Outer Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryColor.copy(alpha = 0.4f), Color.Transparent),
                    center = canvasCenter,
                    radius = radius * 1.5f
                ),
                radius = radius * 1.5f,
                center = canvasCenter
            )

            // Rotational Ring
            drawIntoCanvas {
                it.save()
                it.translate(canvasCenter.x, canvasCenter.y)
                it.rotate(rotation)
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(Color.Transparent, primaryColor, Color.Transparent)
                    ),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(-radius, -radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 8.dp.toPx())
                )
                it.restore()
            }

            // Inner Sphere
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AbyssBlack, VantaBlack),
                    center = canvasCenter,
                    radius = radius * 0.9f
                ),
                radius = radius * 0.9f,
                center = canvasCenter
            )
            
            // Surface Highlight
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent),
                    center = Offset(canvasCenter.x - radius * 0.3f, canvasCenter.y - radius * 0.3f),
                    radius = radius * 0.5f
                ),
                radius = radius * 0.5f,
                center = Offset(canvasCenter.x - radius * 0.3f, canvasCenter.y - radius * 0.3f)
            )

            // Inner Border
            drawCircle(
                color = primaryColor,
                radius = radius * 0.9f,
                center = canvasCenter,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandDeck(onSend: (String) -> Unit, onThoughtStreamToggle: () -> Unit) {
    var text by remember { mutableStateOf("") }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = GlassBackground,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Action Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {}) { Icon(Icons.Default.Mic, "Voice", tint = NeonRedPulse) }
                IconButton(onClick = {}) { Icon(Icons.Default.Image, "Image", tint = CyanIntel) }
                IconButton(onClick = onThoughtStreamToggle) { Icon(Icons.Default.Psychology, "Deep Think", tint = NeonRed) }
                IconButton(onClick = {}) { Icon(Icons.Default.AttachFile, "File", tint = TextSecondary) }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Message Orca...", color = TextSecondary) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = NeonRed,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                
                IconButton(
                    onClick = { 
                        onSend(text)
                        text = ""
                    },
                    enabled = text.isNotBlank()
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (text.isNotBlank()) NeonRed else TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ThoughtStreamPanel(thoughts: List<String>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = AbyssBlack.copy(alpha = 0.8f),
        border = BorderStroke(1.dp, CyanIntel.copy(alpha = 0.3f))
    ) {
        val scrollState = rememberScrollState()
        LaunchedEffect(thoughts.size) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(">> INTERNAL MONOLOGUE", style = MaterialTheme.typography.labelSmall, color = CyanIntel, modifier = Modifier.padding(bottom = 8.dp))
            thoughts.forEach { thought ->
                Text(
                    text = thought,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                    color = CyanIntel.copy(alpha = 0.7f),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}
