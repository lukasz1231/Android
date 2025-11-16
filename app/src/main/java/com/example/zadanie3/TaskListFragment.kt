package com.example.zadanie3

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zadanie3.models.Category
import com.example.zadanie3.models.Task

class TaskListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: TaskAdapter? = null
    private var subtitleVisible: Boolean = false

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

        private val nameTextView: TextView = itemView.findViewById(R.id.task_item_name)
        private val dateTextView: TextView = itemView.findViewById(R.id.task_item_date)
        private val iconImageView: ImageView = itemView.findViewById(R.id.task_item_icon)
        val doneCheckBox: CheckBox = itemView.findViewById(R.id.task_item_checkbox)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            this.task = task
            nameTextView.text = task.name
            dateTextView.text = task.date.toString()

            doneCheckBox.isChecked = task.isDone

            if (task.isDone) {
                nameTextView.paintFlags = nameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                nameTextView.paintFlags = nameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            val iconResId = when (task.category) {
                Category.STUDIES -> R.drawable.ic_university
                Category.HOME -> R.drawable.ic_house
            }
            iconImageView.setImageResource(iconResId)
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

            holder.doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val currentTask = tasks[holder.bindingAdapterPosition]
                currentTask.isDone = isChecked
                holder.bind(currentTask)
                updateSubtitle()
            }
        }

        override fun getItemCount(): Int {
            return tasks.size
        }

        fun setTasks(tasks: List<Task>) {
            this.tasks = tasks
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        subtitleVisible = savedInstanceState?.getBoolean("subtitleVisibleKey", false) ?: false
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("subtitleVisibleKey", subtitleVisible)
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_menu, menu)

        val subtitleItem = menu.findItem(R.id.show_subtitle)
        if (subtitleVisible) {
            subtitleItem.title = getString(R.string.hide_subtitle)
        } else {
            subtitleItem.title = getString(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_task -> {
                val task = Task()
                TaskStorage.addTask(task)
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra(KEY_EXTRA_TASK_ID, task.id)
                startActivity(intent)
                true
            }
            R.id.show_subtitle -> {
                subtitleVisible = !subtitleVisible
                activity?.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun updateSubtitle() {
        val tasks = TaskStorage.getTasks()
        val todoTasksCount = tasks.count { !it.isDone }
        var subtitle: String? = getString(R.string.subtitle_format, todoTasksCount)
        if (!subtitleVisible) {
            subtitle = null
        }
        val appCompatActivity = activity as? AppCompatActivity
        appCompatActivity?.supportActionBar?.subtitle = subtitle
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
        updateSubtitle()
    }
}
