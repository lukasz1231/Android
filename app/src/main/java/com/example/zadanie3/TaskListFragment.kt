package com.example.zadanie3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zadanie3.models.Task
import com.example.zadanie3.MainActivity
import java.util.UUID

class TaskListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: TaskAdapter? = null

    companion object {
        const val KEY_EXTRA_TASK_ID = "com.example.zadanie3.task_id"
    }

    private inner class TaskHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_task, parent, false)
    ), View.OnClickListener {

        private lateinit var task: Task
        val nameTextView: TextView = itemView.findViewById(R.id.task_item_name)
        val dateTextView: TextView = itemView.findViewById(R.id.task_item_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            this.task = task
            nameTextView.text = task.name
            dateTextView.text = task.date.toString()

            if (task.isDone) {
                nameTextView.paintFlags = nameTextView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                nameTextView.paintFlags = nameTextView.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        override fun onClick(v: View) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra(KEY_EXTRA_TASK_ID, task.id)
            startActivity(intent)
        }
    }

    private inner class TaskAdapter(
        private var tasks: List<Task>
    ) : RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return TaskHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        override fun getItemCount(): Int {
            return tasks.size
        }

        fun setTasks(tasks: List<Task>) {
            this.tasks = tasks
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        recyclerView = view.findViewById(R.id.task_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        updateView()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    private fun updateView() {
        val tasks = TaskStorage.getTasks()

        if (adapter == null) {
            adapter = TaskAdapter(tasks)
            recyclerView.adapter = adapter
        } else {
            (adapter as TaskAdapter).setTasks(tasks)
            (adapter as TaskAdapter).notifyDataSetChanged()
        }
    }
}
