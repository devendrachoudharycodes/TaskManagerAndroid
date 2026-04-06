package com.genzsage.taskmanager.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genzsage.taskmanager.data.repository.TaskRepositoryImpl
import com.genzsage.taskmanager.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepositoryImpl
): ViewModel() {
    private val _sortType = MutableStateFlow(SortType.PRIORITY_DESC)

    private val _tasksFlow = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.PRIORITY_DESC -> repository.getAllTasksSortedByPriorityDesc()
            SortType.PRIORITY_ASC -> repository.getAllTasksSortedByPriorityAsc()
            SortType.DUE_DATE_ASC -> repository.getAllTasksSortedByDueDateAsc()
            SortType.DUE_DATE_DESC -> repository.getAllTasksSortedByDueDateDesc()
            SortType.DATE_ADDED_DESC -> repository.getAllTasksSortedByDateAddedDesc()
            SortType.DATE_ADDED_ASC -> repository.getAllTasksSortedByDateAddedAsc()
            SortType.TITLE_ASC -> repository.getAllTasksSortedByTitleAsc()
            SortType.TITLE_DESC -> repository.getAllTasksSortedByTitleDesc()
            SortType.STATUS_ASC -> repository.getAllTasksSortedByStatusAsc()
            SortType.STATUS_DESC -> repository.getAllTasksSortedByStatusDesc()
        }
    }

    val uiState: StateFlow<TasksUiState> = combine(_sortType, _tasksFlow) { sortType, tasks ->
        TasksUiState(
            tasks = tasks,
            currentSortType = sortType
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksUiState()
    )


    fun onSortTypeChanged(newSortType: SortType) {
        _sortType.value = newSortType
    }



    fun onTaskDeleted(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

}