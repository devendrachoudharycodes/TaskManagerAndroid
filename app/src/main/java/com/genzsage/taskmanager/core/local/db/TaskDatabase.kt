package com.genzsage.taskmanager.core.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.genzsage.taskmanager.core.local.dao.TaskDao
import com.genzsage.taskmanager.core.local.entity.TaskEntity


@Database(entities = [ TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}