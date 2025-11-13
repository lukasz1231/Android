// TaskFragment.kt

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
import com.example.zadanie3.models.Task
import databinding.FragmentTaskBinding
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
        // Używamy ViewBinding, zakładając, że layout to fragment_task.xml
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val view = binding.root

        // Powiązanie widoków
        nameField = binding.taskName
        dateButton = binding.taskDate
        doneCheckBox = binding.taskDone

        // Ustawienie początkowych wartości pól na podstawie załadowanego zadania
        nameField.setText(task.name)
        dateButton.text = task.date.toString()
        dateButton.isEnabled = false // Zgodnie z zadaniem
        doneCheckBox.isChecked = task.isDone

        // Obsługa zmiany nazwy (jak w oryginalnym zadaniu)
        nameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.name = s.toString() // Aktualizacja modelu
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Obsługa CheckBox (jak w oryginalnym zadaniu)
        doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked // Aktualizacja modelu
        }

        return view
    }
    fun bind(task: Task) {
        this.task = task
        itemBinding.taskItemName.text = task.name
        itemBinding.taskItemDate.text = task.date.toString()

        // Opcjonalne: Wizualizacja statusu wykonania (lepszy feedback)
        if (task.isDone) {
            // Ustawienie flagi tekstowej, aby tekst był przekreślony
            itemBinding.taskItemName.paintFlags =
                itemBinding.taskItemName.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            // Usunięcie flagi przekreślenia
            itemBinding.taskItemName.paintFlags =
                itemBinding.taskItemName.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}