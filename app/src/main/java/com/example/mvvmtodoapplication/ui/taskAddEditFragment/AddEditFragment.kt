package com.example.mvvmtodoapplication.ui.taskAddEditFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mvvmtodoapplication.R
import com.example.mvvmtodoapplication.databinding.FragmentAddEditTaskBinding
import com.example.mvvmtodoapplication.model.Task
import com.example.mvvmtodoapplication.ui.TaskFragment.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditFragment : Fragment(R.layout.fragment_add_edit_task) {
    lateinit var binding:FragmentAddEditTaskBinding
    val viewModel: AddEditViewModel by viewModels()
    val args:AddEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditTaskBinding.bind(view)

        args.task?.let { task->
            binding.apply {
                etAddEdit.setText(task.name)
                cbAddImportantTask.isChecked = task.important
                cbAddImportantTask.jumpDrawablesToCurrentState()
                tvDateCreated.text = "Created : ${task.createDateFormat}"
            }
        }


        binding.fabAddTask.setOnClickListener {
            if(args.task ==null){
                addTask()
            }else{
                editTask(args.task!!)
            }

        }


    }

    private fun addTask(){
        var task:Task?=null
        val name = binding.etAddEdit.text.toString()
        val importent = binding.cbAddImportantTask.isChecked
        val created = System.currentTimeMillis()
        if(name.isNotEmpty()){
             task = Task(name,importent,false,created)
        }else{
            binding.etAddEdit.apply {
                val color = resources.getColor(R.color.red)
                setHintTextColor(color)
//                editText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        }

        task?.let {
            viewModel.add(task)
            val action = AddEditFragmentDirections.actionAddEditFragmentToTasksFragment("add")
            findNavController().navigate(action)


        }


    }

    private fun editTask(task:Task){
        val newName = binding.etAddEdit.text.toString()
        val newTask = task.copy(name =newName,important = binding.cbAddImportantTask.isChecked )

        if(newName.isNotEmpty()){
            viewModel.edit(newTask)
            val action = AddEditFragmentDirections.actionAddEditFragmentToTasksFragment("edit")
            findNavController().navigate(action)
        }else{
            binding.etAddEdit.apply {
                setHintTextColor(resources.getColor(R.color.red))
            }
        }

    }

}