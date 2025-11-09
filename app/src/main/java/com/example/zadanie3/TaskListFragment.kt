package com.example.zadanie3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var recyclerView: RecyclerView
    // private var adapter: TaskAdapter? = null

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}