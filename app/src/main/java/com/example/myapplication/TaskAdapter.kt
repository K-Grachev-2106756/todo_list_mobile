package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskAdapter(private val tasks: MutableList<Task>, private val context: Context) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("tasks_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    init {
        loadTasks()
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskTitle.text = task.title
        holder.btnDelete.setOnClickListener {
            tasks.removeAt(position)
            notifyItemRemoved(position)
            saveTasks()
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
        saveTasks()
    }

    private fun saveTasks() {
        val json = gson.toJson(tasks)
        sharedPreferences.edit().putString("tasks_list", json).apply()
    }

    private fun loadTasks() {
        val json = sharedPreferences.getString("tasks_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            val loadedTasks: MutableList<Task> = gson.fromJson(json, type)
            tasks.clear()
            tasks.addAll(loadedTasks)
        }
    }
}
