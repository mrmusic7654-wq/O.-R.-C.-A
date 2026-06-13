package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ForkRight
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Message(val text: String, val isUser: Boolean, val isSystem: Boolean = false, val isThought: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(activeAgent: String, onOpenDrawer: () -> Unit) {
    val messages = remember { mutableStateListOf(
        Message("System Online. Awaiting directive.", isUser = false, isSystem = true),
        Message("What is your current status?", isUser = true),
        Message("All subsystems optimal. AutoPilot is disabled.", isUser = false)
    )}
    var isTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().background(VantaBlack)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = { Text("$activeAgent // SESSION", style = MaterialTheme.typography.labelLarge, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.ForkRight, "Fork Session", tint = TextSecondary) }
                    IconButton(onClick = {}) { Icon(Icons.Default.Timeline, "Task Timeline", tint = TextSecondary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    MessageBubble(msg)
                }
                if (isTyping) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            // Input Bar
            CommandDeck(
                onSend = { text ->
                    messages.add(Message(text, isUser = true))
                    coroutineScope.launch {
                        listState.animateScrollToItem(messages.size - 1)
                        isTyping = true
                        delay(1500)
                        isTyping = false
                        messages.add(Message("> Processing input parameters...", isUser = false, isThought = true))
                        listState.animateScrollToItem(messages.size - 1)
                        delay(1000)
                        messages.add(Message("I have received your directive. Proceeding with execution.", isUser = false))
                        listState.animateScrollToItem(messages.size - 1)
                    }
                },
                onThoughtStreamToggle = {}
            )
        }
    }
}

@Composable
fun MessageBubble(msg: Message) {
    if (msg.isSystem) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = msg.text,
                style = MaterialTheme.typography.labelSmall,
                color = CyanIntel,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        return
    }

    if (msg.isThought) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
                text = msg.text,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = TextSecondary.copy(alpha = 0.7f)
            )
        }
        return
    }

    val alignment = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (msg.isUser) AbyssBlack else NeonRed.copy(alpha = 0.1f)
    val borderColor = if (msg.isUser) GlassBorder else NeonRed.copy(alpha = 0.3f)
    val shape = if (msg.isUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Surface(
            shape = shape,
            color = bgColor,
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = msg.text,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    val transition = rememberInfiniteTransition(label = "typing")
    val dotColor by transition.animateColor(
        initialValue = NeonRed.copy(alpha = 0.3f),
        targetValue = NeonRed,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "typingColor"
    )

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Surface(
            shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp),
            color = NeonRed.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, NeonRed.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(6.dp).background(dotColor, RoundedCornerShape(50)))
                Box(modifier = Modifier.size(6.dp).background(dotColor, RoundedCornerShape(50)))
                Box(modifier = Modifier.size(6.dp).background(dotColor, RoundedCornerShape(50)))
            }
        }
    }
}
