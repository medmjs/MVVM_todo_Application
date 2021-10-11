package com.example.mvvmtodoapplication.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvmtodoapplication.data.TaskDB
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "tasks_table")
@Parcelize
data class Task(
    val name: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {
    val createDateFormat: String
        get() = DateFormat.getTimeInstance().format(created)


}