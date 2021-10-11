package com.example.mvvmtodoapplication.di

import android.app.Application
import androidx.room.Room
import com.example.mvvmtodoapplication.data.TaskDB
import com.example.mvvmtodoapplication.data.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.supervisorScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: TaskDB.Callback
    ) = Room.databaseBuilder(app, TaskDB::class.java, "task_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()


    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDB) = db.getDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope