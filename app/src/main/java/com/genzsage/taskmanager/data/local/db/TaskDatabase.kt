package com.genzsage.taskmanager.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.genzsage.taskmanager.data.local.dao.TaskDao
import com.genzsage.taskmanager.data.local.entity.TaskEntity


@Database(entities = [ TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}