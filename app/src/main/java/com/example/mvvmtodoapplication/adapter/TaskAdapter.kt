package com.example.mvvmtodoapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodoapplication.databinding.ItemTaskBinding
import com.example.mvvmtodoapplication.model.Task

//class TaskAdapter():RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
class TaskAdapter(val listener:OnItemClickListener):ListAdapter<Task,TaskAdapter.TaskViewHolder>(callbackDiffUtil){


//    val diffUtil =AsyncListDiffer(this,callbackDiffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        val task =diffUtil.currentList[position]
        val task =getItem(position)
        holder.bind(task)
    }

//    override fun getItemCount(): Int {
//        return diffUtil.currentList.size
//    }

    inner class TaskViewHolder(private val binding:ItemTaskBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }

                cbItemTask.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onCheckBoxChecked(task,cbItemTask.isChecked)
                    }
                }

            }
        }

        fun bind(task:Task){
            binding.apply {
                cbItemTask.isChecked = task.completed
                tvPriorityItemTask.text = task.name
                tvPriorityItemTask.paint.isUnderlineText = task.important
                tvPriorityItemTask.paint.isStrikeThruText = task.completed
                ivItemTask.isVisible = task.important

            }

        }



    }

    interface OnItemClickListener{
        public fun onItemClick(task:Task)
        public fun onCheckBoxChecked(task:Task,checked:Boolean)
    }

    companion object {

        val callbackDiffUtil = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }
}