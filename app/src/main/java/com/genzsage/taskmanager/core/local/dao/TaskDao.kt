package com.genzsage.taskmanager.core.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.genzsage.taskmanager.core.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks " +
                    "ORDER BY priority DESC;")
    fun getAllTasksPriority(): Flow<List<TaskEntity>>

    @Insert
    fun addTask(task: TaskEntity)

    @Delete
    fun deleteTask(id: TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)
}