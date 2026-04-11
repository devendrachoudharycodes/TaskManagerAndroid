package com.genzsage.taskmanager.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.genzsage.taskmanager.domain.model.Task
import kotlinx.coroutines.launch

@Composable
fun TasksScreen(modifier: Modifier = Modifier.fillMaxSize(),
                tasksViewModel: TaskViewModel=hiltViewModel(),
                onEdit: (Task) -> Unit) {
    val sortType=tasksViewModel.uiState.collectAsState().value.currentSortType

    val tabs = listOf("Ongoing Tasks", "Completed Tasks")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column {

    PrimaryTabRow (selectedTabIndex = pagerState.currentPage) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = { Text(text = title) }
            )
        }
    }

    HorizontalPager(
        state = pagerState
    ) { page ->

            when (page) {
                0 ->OngoingTasks(
                    tasksViewModel.uiState.collectAsState()
                        .value.tasks.filter { task -> !task.isCompleted },
                    onDelete = { task -> tasksViewModel.onTaskDeleted(task) },
                    onMarkComplete = { task -> tasksViewModel.onTaskCompletedOrNot(task) },
                    onEdit = onEdit
                )
                1 ->CompletedTasks(
                    tasksViewModel.uiState.collectAsState()
                        .value.tasks.filter { task -> task.isCompleted },
                    onDelete = { task -> tasksViewModel.onTaskDeleted(task) },
                    onMarkInComplete = { task -> tasksViewModel.onTaskCompletedOrNot(task) },
                    onEdit = onEdit
                )
            }

    }
}}

@Composable
fun OngoingTasks(tasks:List<Task>,onDelete:(Task)-> Unit,onMarkComplete:(Task)-> Unit,onEdit:(Task)->Unit) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onDelete = { onDelete(task) },
                onChecked = { onMarkComplete(task) },
                onEdit = { onEdit(task)},
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun CompletedTasks(tasks:List<Task>,onDelete:(Task)-> Unit,onMarkInComplete:(Task)->Unit,onEdit:(Task)->Unit) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onDelete = { onDelete(task) },
                onChecked = {
                    onMarkInComplete(task)
                },
                onEdit = {onEdit(task)}
            )
            Spacer(modifier = Modifier.height(4.dp))

        }
    }
}