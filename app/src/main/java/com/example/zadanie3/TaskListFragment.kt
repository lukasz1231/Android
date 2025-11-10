package com.example.zadanie3

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zadanie3.models.Task
import databinding.FragmentTaskListBinding
import databinding.ListItemTaskBinding

class TaskListFragment : Fragment() {


    private var _binding: FragmentTaskListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var recyclerView: RecyclerView
    private var adapter: TaskAdapter? = null

    /
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.taskRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)


        // updateView()

        return view
    }

    // Klasa ViewHolder (Punkt 23)
    private inner class TaskHolder(
        private val itemBinding: ListItemTaskBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var task: Task

        fun bind(task: Task) {
            this.task = task
            itemBinding.taskItemName.text = task.name
            itemBinding.taskItemDate.text = task.date.toString()

        }
    }

    // Klasa Adaptera (Punkt 24)
    private inner class TaskAdapter(
        var tasks: List<Task>
    ) : RecyclerView.Adapter<TaskHolder>() {

        // Tworzenie ViewHoldera i "pompowanie" layoutu elementu listy
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            // Użycie LayoutInflater z kontekstu rodzica (parent)
            val inflater = LayoutInflater.from(parent.context)

            val itemBinding = ListItemTaskBinding.inflate(inflater, parent, false)

            return TaskHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        override fun getItemCount() = tasks.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}