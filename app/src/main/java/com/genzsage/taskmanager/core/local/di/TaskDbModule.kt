package com.genzsage.taskmanager.core.local.di

import android.content.Context
import androidx.room.Room
import com.genzsage.taskmanager.core.local.dao.TaskDao
import com.genzsage.taskmanager.core.local.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
object TaskDbModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext
                  context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideDao(db: TaskDatabase): TaskDao {
        return db.taskDao()
    }
}