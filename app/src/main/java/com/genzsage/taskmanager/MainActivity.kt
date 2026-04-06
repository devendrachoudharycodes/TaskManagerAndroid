package com.genzsage.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.genzsage.taskmanager.presentation.home.TasksScreen
import com.genzsage.taskmanager.presentation.newtask.CreateOrUpdateTask
import com.genzsage.taskmanager.ui.theme.TaskManagerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
            TasksScreen(
                onCreateTaskClick = {},
                onNavigateToSettings = {  }
            )         }
        }
    }
}
