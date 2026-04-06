package com.genzsage.taskmanager.presentation.newtask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genzsage.taskmanager.domain.model.Task
import com.genzsage.taskmanager.presentation.home.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrUpdateTask(
    modifier: Modifier = Modifier,
    task: Task = Task(0, false, "", "", 0L, 0L, 5),
    viewModel: TaskCreateOrUpdateViewModel = hiltViewModel(),
    onBack: () -> Unit = {} // Callback for navigation
) {
    // 1. Initialize the ViewModel with the passed task ONLY once
    LaunchedEffect(task) {
        viewModel.updateTask { task }
    }

    // 2. Collect the state from ViewModel
    val taskState by viewModel.taskState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (task.id == 0) "New Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.saveTask()
                onBack()
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save Task")
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Task Title Input
            OutlinedTextField(
                value = taskState.title,
                onValueChange = { newTitle ->
                    viewModel.updateTask { it.copy(title = newTitle) }
                },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Task Description Input
            OutlinedTextField(
                value = taskState.description,
                onValueChange = { newDesc ->
                    viewModel.updateTask { it.copy(description = newDesc) }
                },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Task Completion Toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = taskState.isCompleted,
                    onCheckedChange = { completed ->
                        viewModel.updateTask { it.copy(isCompleted = completed) }
                    }
                )
                Text("Mark as Completed")
            }
        }
    }
}