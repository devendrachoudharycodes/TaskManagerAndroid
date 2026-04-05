package com.genzsage.taskmanager.core.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "tasks"
)
data class TaskEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val isCompleted: Boolean ,
    val title: String,
    val description: String,
    val timeDateAdded: Long,
    val timeDateDue:Long,
    val priority: Int
)


