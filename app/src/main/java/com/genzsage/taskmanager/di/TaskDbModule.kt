package com.genzsage.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.genzsage.taskmanager.data.local.dao.TaskDao
import com.genzsage.taskmanager.data.local.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
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