package com.example.zadanie3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.zadanie3.databinding.FragmentTaskBinding
import com.example.zadanie3.models.Task
import java.util.UUID

class TaskFragment : Fragment() {

    private lateinit var task: Task

    private lateinit var nameField: EditText
    private lateinit var dateButton: Button
    private lateinit var doneCheckBox: CheckBox

    private var _binding: FragmentTaskBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    companion object {
        private const val ARG_TASK_ID = "task_id"

        fun newInstance(taskId: UUID): TaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID, taskId)
            }
            return TaskFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskId = arguments?.getSerializable(ARG_TASK_ID) as? UUID
            ?: throw IllegalStateException("TaskFragment must be supplied with a Task ID.")

        task = TaskStorage.getTask(taskId)
            ?: throw IllegalStateException("Task with ID $taskId not found.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val view = binding.root

        nameField = binding.taskName
        dateButton = binding.taskDate
        doneCheckBox = binding.taskDone

        nameField.setText(task.name)
        dateButton.text = task.date.toString()
        dateButton.isEnabled = false
        doneCheckBox.isChecked = task.isDone

        nameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.name = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
