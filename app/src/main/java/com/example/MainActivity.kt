package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ui.navigation.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                OrcaApp()
            }
        }
    }
}

@Composable
fun OrcaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isTopLevelDestination = BottomNavItems.any { it.route == currentRoute }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    
    var agents by remember { mutableStateOf(listOf("ORCA v2.0", "CIPHER v1.1", "NEXUS v3.0", "SENTINEL v1.0")) }
    var activeAgent by remember { mutableStateOf(agents.first()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = AbyssBlack,
                drawerContentColor = TextPrimary,
                modifier = Modifier.width(300.dp)
            ) {
                Column(modifier = Modifier.fillMaxHeight().padding(16.dp)) {
                    Text("AGENT SWARM", style = MaterialTheme.typography.headlineMedium, color = CyanIntel)
                    Text("SELECT ACTIVE INSTANCE", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(agents) { agent ->
                            val isSelected = agent == activeAgent
                            
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 1f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                            val containerColor by animateColorAsState(
                                targetValue = if (isSelected) NeonRed.copy(alpha = 0.15f) else Color.Transparent,
                                animationSpec = tween(300)
                            )
                            val borderColor by animateColorAsState(
                                targetValue = if (isSelected) NeonRed.copy(alpha = 0.5f) else Color.Transparent,
                                animationSpec = tween(300)
                            )

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = if(isSelected) 8.dp else 0.dp)
                                    .scale(scale),
                                shape = MaterialTheme.shapes.small,
                                color = containerColor,
                                border = BorderStroke(1.dp, borderColor),
                                onClick = {
                                    activeAgent = agent
                                    coroutineScope.launch { drawerState.close() }
                                }
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.TripOrigin, 
                                        contentDescription = null, 
                                        tint = if (isSelected) NeonRed else TextSecondary.copy(alpha = 0.5f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(agent, style = MaterialTheme.typography.labelLarge, color = if (isSelected) TextPrimary else TextSecondary)
                                }
                            }
                        }
                    }
                    
                    Button(
                        onClick = { agents = agents + "AGENT v${(1..9).random()}.${(0..9).random()}" },
                        colors = ButtonDefaults.buttonColors(containerColor = GlassBackground),
                        border = BorderStroke(1.dp, CyanIntel),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = CyanIntel)
                        Spacer(Modifier.width(8.dp))
                        Text("DEPLOY NEW AGENT", color = CyanIntel, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = VantaBlack,
            bottomBar = {
                if (isTopLevelDestination) {
                    NavigationBar(
                        containerColor = AbyssBlack,
                        contentColor = TextPrimary
                    ) {
                        BottomNavItems.forEach { screen ->
                            val selected = currentRoute == screen.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) screen.iconFilled else screen.iconOutlined,
                                        contentDescription = screen.title
                                    )
                                },
                                label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NeonRed,
                                    selectedTextColor = NeonRed,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary,
                                    indicatorColor = NeonRed.copy(alpha = 0.2f)
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Orb.route,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { 
                    fadeIn(tween(400)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(400, easing = FastOutSlowInEasing)) 
                },
                exitTransition = { 
                    fadeOut(tween(400)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(400, easing = FastOutSlowInEasing)) 
                }
            ) {
                composable(Screen.Orb.route) {
                    OrbScreen(
                        activeAgent = activeAgent,
                        onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                }
                composable(Screen.Chat.route) {
                    ChatScreen(activeAgent = activeAgent, onOpenDrawer = { coroutineScope.launch { drawerState.open() } })
                }
                composable(Screen.Memory.route) {
                    MemoryStreamScreen(activeAgent = activeAgent, onOpenDrawer = { coroutineScope.launch { drawerState.open() } })
                }
                composable(Screen.WarRoom.route) {
                    WarRoomScreen(activeAgent = activeAgent, onOpenDrawer = { coroutineScope.launch { drawerState.open() } })
                }
                composable(Screen.Metrics.route) {
                    MetricsScreen(activeAgent = activeAgent, onOpenDrawer = { coroutineScope.launch { drawerState.open() } })
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}
