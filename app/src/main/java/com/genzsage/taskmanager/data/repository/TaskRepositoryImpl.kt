package com.genzsage.taskmanager.data.repository

import com.genzsage.taskmanager.data.local.dao.TaskDao
import com.genzsage.taskmanager.data.local.entity.TaskEntity
import com.genzsage.taskmanager.domain.model.Task
import com.genzsage.taskmanager.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Optional
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {

    override fun getAllTasksSortedByPriorityDesc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByPriorityDesc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByPriorityAsc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByPriorityAsc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByDueDateAsc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByDueDateAsc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByDueDateDesc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByDueDateDesc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByDateAddedDesc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByDateAddedDesc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByDateAddedAsc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByDateAddedAsc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByTitleAsc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByTitleAsc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByTitleDesc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByTitleDesc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByStatusAsc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByStatusAsc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasksSortedByStatusDesc(): Flow<List<Task>> {
        return dao.getAllTasksSortedByStatusDesc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return dao.getTaskWithId(id)
    }


    override suspend fun addTask(task: Task) {
        dao.addTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task.toEntity())
    }
}

// =================================================================
// MAPPING EXTENSION FUNCTIONS
// =================================================================

private fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        isCompleted = isCompleted,
        title = title,
        description = description,
        timeDateAdded = timeDateAdded,
        timeDateDue = timeDateDue,
        priority = priority
    )
}

private fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        isCompleted = isCompleted,
        title = title,
        description = description,
        timeDateAdded = timeDateAdded,
        timeDateDue = timeDateDue,
        priority = priority
    )
}