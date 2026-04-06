package com.genzsage.taskmanager.presentation.newtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genzsage.taskmanager.data.repository.TaskRepositoryImpl
import com.genzsage.taskmanager.domain.model.Task
import com.genzsage.taskmanager.domain.repository.TaskRepository // Use the Interface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskCreateOrUpdateViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryImpl
): ViewModel() {

    private val _task = MutableStateFlow(Task(0, false, "", "", 0L, 0L, 5))
    val taskState = _task.asStateFlow()

    fun updateTask(transform: (Task) -> Task) {
        _task.update { transform(it) }
    }

    fun saveTask() {
        val currentTask = _task.value
        viewModelScope.launch {
            if (currentTask.id == 0) {
                taskRepository.addTask(currentTask)
            } else {
                taskRepository.updateTask(currentTask)
            }
        }
    }
}