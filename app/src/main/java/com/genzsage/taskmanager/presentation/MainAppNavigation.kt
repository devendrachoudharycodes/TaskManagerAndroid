package com.genzsage.taskmanager.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.genzsage.taskmanager.R
import com.genzsage.taskmanager.presentation.home.TasksScreen
import com.genzsage.taskmanager.presentation.newtask.CreateOrUpdateTask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppNavigation(
    modifier: Modifier = Modifier,
    onThemeChange: () -> Unit = {},
    isDarkMode: Boolean = false
) {
    val navController: NavHostController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "Home"

    // FIX 2: Use startsWith for dynamic routes
    val screenTitle = when {
        currentRoute == "Home" -> "All Tasks"
        currentRoute.startsWith("add_task") -> "New Task"
        currentRoute == "Settings" -> "Settings"
        else -> "Task Manager"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                actions = {
                    IconButton(onClick = onThemeChange) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "Home",
                    onClick = {
                        navController.navigate("Home") {
                            // FIX 3: Standard bottom nav state saving
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Task, null) },
                    label = { Text("Tasks") }
                )

                NavigationBarItem(
                    // FIX 2: Use startsWith for selected state
                    selected = currentRoute.startsWith("add_task"),
                    onClick = {
                        // FIX 1: Use the correct base route
                        navController.navigate("add_task") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Add, null) },
                    label = { Text("Create") }
                )

                NavigationBarItem(
                    selected = currentRoute == "Settings",
                    onClick = {
                        navController.navigate("Settings") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Settings, null) },
                    label = { Text("Settings") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            NavHost(
                navController = navController,
                startDestination = "Home"
            ) {
                composable("Home") {
                    TasksScreen(
                        onEdit = { task ->
                            Log.e("edit","navcontroller")
                            navController.navigate("add_task?taskId=${task.id}")
                        }
                    )
                }
                composable(
                    route = "add_task?taskId={taskId}",
                    arguments = listOf(navArgument("taskId") {
                        type = NavType.IntType
                        defaultValue = 0
                    })
                ) { backStackEntry ->
                    val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0

                    CreateOrUpdateTask(
                        taskId = taskId,
                        onSaveSuccess = {
                            // Pop back to home to avoid infinite stack of screens
                            navController.popBackStack("Home", inclusive = false)
                        }
                    )
                }
                composable("Settings") {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null
                    )
                }
            }
        }
    }
}