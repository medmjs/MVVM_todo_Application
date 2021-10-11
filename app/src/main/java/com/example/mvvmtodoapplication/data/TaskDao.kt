package com.example.mvvmtodoapplication.data

import androidx.room.*
import com.example.mvvmtodoapplication.model.Task

import com.example.mvvmtodoapplication.data.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTask(
        searchQuery: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean
    ): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getTaskOrderByName(searchQuery, hideCompleted)
            SortOrder.BY_DATE -> getTaskOrderByCreated(searchQuery, hideCompleted)
        }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upset(task: Task)

    @Query("SELECT * FROM TASKS_TABLE WHERE   (completed !=:hidCompleted OR completed =0) AND name LIKE '%'||:searchQuery || '%' ORDER BY important DESC,name")
    fun getTaskOrderByName(searchQuery: String, hidCompleted: Boolean): Flow<List<Task>>


    @Query("SELECT * FROM TASKS_TABLE WHERE   (completed !=:hidCompleted OR completed =0) AND name LIKE '%'||:searchQuery || '%' ORDER BY important DESC,created")
    fun getTaskOrderByCreated(searchQuery: String, hidCompleted: Boolean): Flow<List<Task>>

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM TASKS_TABLE WHERE completed=1")
    suspend fun deleteAll()
}