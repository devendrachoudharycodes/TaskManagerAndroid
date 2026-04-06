package com.genzsage.taskmanager.domain.model

data class Task(
    val id: Int = 0,
    val isCompleted: Boolean,
    val title: String,
    val description: String,
    val timeDateAdded: Long,
    val timeDateDue: Long,
    val priority: Int
)