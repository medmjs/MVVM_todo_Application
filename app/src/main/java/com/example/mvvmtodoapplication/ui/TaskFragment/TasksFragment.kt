package com.example.mvvmtodoapplication.ui.TaskFragment

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodoapplication.R
import com.example.mvvmtodoapplication.adapter.TaskAdapter
import com.example.mvvmtodoapplication.databinding.FragmentTasksBinding
import com.example.mvvmtodoapplication.data.SortOrder
import com.example.mvvmtodoapplication.model.Task
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment() : Fragment(R.layout.fragment_tasks), TaskAdapter.OnItemClickListener {

    lateinit var binding: FragmentTasksBinding
    val viewModel: TaskViewModel by viewModels()
    lateinit var taskAdapter: TaskAdapter

    lateinit var searchView: SearchView

    private val args: TasksFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTasksBinding.bind(view)

        args.action?.let { action ->
            checkArgs(action)
        }


        taskAdapter = TaskAdapter(this)

        setUpRecycleView()

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
//            taskAdapter.diffUtil.submitList(it)
            taskAdapter.submitList(it)
        })

        binding.fabTasks.setOnClickListener {
            goToAddFragment()
        }

        setHasOptionsMenu(true)

        swipingForDelete()


    }

    override fun onItemClick(task: Task) {

        val action = TasksFragmentDirections.actionTasksFragmentToAddEditFragment(task)
        findNavController().navigate(action)

    }

    override fun onCheckBoxChecked(task: Task, isChecked: Boolean) {
        viewModel.updateCheckBox(task, isChecked)
    }

    private fun setUpRecycleView() {
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
            setHasFixedSize(true)
        }
    }

    private fun swipingForDelete() {

        binding.apply {
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteTask(task)

                    Snackbar.make(binding.root, "the task Delete", Snackbar.LENGTH_LONG)
                        .setAction("Undo",
                            View.OnClickListener {
                                viewModel.addNewTask(task)
                            }).show()
                }
            }).attachToRecyclerView(rvTasks)
        }


    }


    private fun goToAddFragment() {

        val action = TasksFragmentDirections.actionTasksFragmentToAddEditFragment(null)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.task_menu, menu)

        val searchItem = menu.findItem(R.id.misearch)
        searchView = searchItem.actionView as SearchView


        //for change between landscap to rotarot
        val pindingSearch = viewModel.searchQuery.value
        if (pindingSearch.isNotEmpty() && pindingSearch != null) {
            searchItem.expandActionView()
            searchView.setQuery(pindingSearch, false)
        }



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchQuery.value = newText
                }
                return true
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.mi_hide_completed).isChecked =
                viewModel.preferanceFlow.first().hide
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.misortname -> {
//                viewModel.sortOrder.value = SortOrder.BY_NAME
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.misortdate -> {
//                viewModel.sortOrder.value = SortOrder.BY_DATE
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true

            }
            R.id.midelete_completed_task -> {

//                val action = TasksFragmentDirections.actionGlobalDeleteAllCompletedDialog()
//                findNavController().navigate(action)

                val dialog = createDialog().show()


//                for (task in taskAdapter.currentList) {
//                    viewModel.deleteTask(task)
//                }

                true
            }
            R.id.mi_hide_completed -> {
                item.isChecked = !item.isChecked
//                viewModel.hideCompleted.value = item.isChecked
                viewModel.onhideCompletedSelected(item.isChecked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkArgs(action: String) {

        when (action) {
            "add" -> {
                Snackbar.make(binding.root, "Add NewTask ", Snackbar.LENGTH_LONG).show()
            }
            "edit" -> {
                Snackbar.make(binding.root, "Task is Update", Snackbar.LENGTH_LONG).show()
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        searchView.setOnQueryTextListener(null)
    }


    private fun createDialog(): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setMessage("Are you ture to delete  all Completed Task ?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAllCompletedTask()
            }
            .create()
    }

}