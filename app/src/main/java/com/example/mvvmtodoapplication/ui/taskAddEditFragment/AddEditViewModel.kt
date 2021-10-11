package com.example.mvvmtodoapplication.ui.taskAddEditFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmtodoapplication.data.TaskDao
import com.example.mvvmtodoapplication.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    val taskDao:TaskDao
):ViewModel() {


    fun add(task:Task)=viewModelScope.launch {
        taskDao.upset(task)
    }

    fun edit(task:Task)=viewModelScope.launch {
        taskDao.update(task)
    }





}