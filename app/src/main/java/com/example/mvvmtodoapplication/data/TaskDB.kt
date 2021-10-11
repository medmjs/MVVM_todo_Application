package com.example.mvvmtodoapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mvvmtodoapplication.di.ApplicationScope
import com.example.mvvmtodoapplication.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.locks.Lock
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class],version = 1)
abstract class TaskDB() : RoomDatabase() {

    abstract fun getDao(): TaskDao


    class Callback @Inject constructor(
        private val database: Provider<TaskDB>,
        @ApplicationScope
        private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().getDao()

            applicationScope.launch {
                dao.upset(Task("go toschool"))
                dao.upset(Task("go tohome"))
                dao.upset(Task("go toschooljob", important = true))
                dao.upset(Task("go toschool5", completed = true))
                dao.upset(Task("go toschool9"))
            }


        }
    }


}