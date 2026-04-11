package com.genzsage.taskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.genzsage.taskmanager.data.local.entity.TaskEntity
import com.genzsage.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.Optional

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY priority DESC")
    fun getAllTasksSortedByPriorityDesc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY priority ASC")
    fun getAllTasksSortedByPriorityAsc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY timeDateDue ASC")
    fun getAllTasksSortedByDueDateAsc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY timeDateDue DESC")
    fun getAllTasksSortedByDueDateDesc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY timeDateAdded DESC")
    fun getAllTasksSortedByDateAddedDesc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY timeDateAdded ASC")
    fun getAllTasksSortedByDateAddedAsc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY title ASC")
    fun getAllTasksSortedByTitleAsc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY title DESC")
    fun getAllTasksSortedByTitleDesc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, priority DESC")
    fun getAllTasksSortedByStatusAsc(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY isCompleted DESC, priority ASC")
    fun getAllTasksSortedByStatusDesc(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskWithId(id: Int): Task?
}