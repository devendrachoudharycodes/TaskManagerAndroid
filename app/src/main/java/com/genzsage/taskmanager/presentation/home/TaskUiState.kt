package com.genzsage.taskmanager.presentation.home

import com.genzsage.taskmanager.domain.model.Task

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val currentSortType: SortType = SortType.PRIORITY_DESC
)