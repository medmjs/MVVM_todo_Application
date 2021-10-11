package com.example.mvvmtodoapplication.ui.TaskFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmtodoapplication.data.*
import com.example.mvvmtodoapplication.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    val preferancesManager:PreferencesManager,
    val dao: TaskDao
) : ViewModel() {


    val searchQuery = MutableStateFlow("")
//    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
//    val hideCompleted = MutableStateFlow(false)

    val preferanceFlow = preferancesManager.preferencesFlow


//    private val taskFlow = searchQuery.flatMapLatest {
//        it?.let {
//            dao.getTask(it)
//        }
//    }

//    private val taskFlow = combine(
//        searchQuery,
//        sortOrder,
//        hideCompleted
//    ) {query,sort,hide ->
//        Triple(query,sort,hide)
//    }.flatMapLatest {
//        dao.getTask(it.first,it.second,it.third)
//    }


    private val taskFlow= combine(
        searchQuery,preferanceFlow
    ){query,filterPreferances ->
        Pair(query, filterPreferances)
    }.flatMapLatest { (query,filter) ->
        dao.getTask(query,filter.sortOrder,filter.hide)
    }


    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferancesManager.updateSortOrder(sortOrder)
    }


    fun onhideCompletedSelected(hide: Boolean) = viewModelScope.launch {
        preferancesManager.updateHideCompleted(hide)
    }




    val tasks = taskFlow.asLiveData()


//    val tasks = dao.getTask("").asLiveData()

//Update data
    fun updateTask(task: Task)=viewModelScope.launch { dao.update(task)}

    fun updateCheckBox(task: Task,isChecked: Boolean)=viewModelScope.launch {
        dao.update(task.copy(completed = isChecked))
    }


    //Delete Task
   fun deleteTask(task: Task) = viewModelScope.launch { dao.delete(task) }

    //Add Task
    fun addNewTask(task:Task) = viewModelScope.launch {
        dao.upset(task)
    }


    fun deleteAllCompletedTask()=viewModelScope.launch{
        dao.deleteAll()
    }

}
