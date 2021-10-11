package com.example.mvvmtodoapplication.ui.TaskFragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialog : DialogFragment() {
    val viewModel: TaskViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       return  AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setMessage("Are you ture to delete  all Completed Task ?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAllCompletedTask()
            }
            .create()
    }
}