package com.genzsage.taskmanager.domain.repository

import com.genzsage.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.Optional

interface TaskRepository {

    //read operations
    fun getAllTasksSortedByPriorityDesc(): Flow<List<Task>>
    fun getAllTasksSortedByPriorityAsc(): Flow<List<Task>>

    fun getAllTasksSortedByDueDateAsc(): Flow<List<Task>>
    fun getAllTasksSortedByDueDateDesc(): Flow<List<Task>>

    fun getAllTasksSortedByDateAddedDesc(): Flow<List<Task>>
    fun getAllTasksSortedByDateAddedAsc(): Flow<List<Task>>

    fun getAllTasksSortedByTitleAsc(): Flow<List<Task>>
    fun getAllTasksSortedByTitleDesc(): Flow<List<Task>>

    fun getAllTasksSortedByStatusAsc(): Flow<List<Task>>
    fun getAllTasksSortedByStatusDesc(): Flow<List<Task>>

    suspend fun getTaskById(id: Int): Task?

    //write operations

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}