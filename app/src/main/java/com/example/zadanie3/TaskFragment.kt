package com.example.zadanie3

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.zadanie3.databinding.FragmentTaskBinding
import com.example.zadanie3.models.Category
import com.example.zadanie3.models.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class TaskFragment : Fragment() {

    private lateinit var task: Task
    private lateinit var nameField: EditText
    private lateinit var dateField: EditText
    private lateinit var doneCheckBox: CheckBox
    private lateinit var categorySpinner: Spinner

    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var dateFormat: SimpleDateFormat

    private var _binding: FragmentTaskBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    companion object {
        private const val ARG_TASK_ID = "task_id"
        val DEFAULT_NEW_TASK_CATEGORY = Category.STUDIES

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

        task = TaskStorage.getTask(taskId) ?: run {
            val newTask = Task()
            newTask.category = DEFAULT_NEW_TASK_CATEGORY
            newTask
        }
    }

    private fun setupDateFieldValue(date: Date) {
        val locale = Locale("pl", "PL")
        dateFormat = SimpleDateFormat("dd.MM.yyyy", locale)
        dateField.setText(dateFormat.format(date))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val view = binding.root

        nameField = binding.taskName
        dateField = binding.taskDate
        doneCheckBox = binding.taskDone
        categorySpinner = binding.taskCategory

        nameField.setText(task.name)
        doneCheckBox.isChecked = task.isDone
        setupDateFieldValue(task.date)

        calendar.time = task.date

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            setupDateFieldValue(calendar.time)
            task.date = calendar.time
        }

        dateField.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        categorySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Category.entries.toTypedArray()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                task.category = Category.entries.toTypedArray()[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        categorySpinner.setSelection(task.category.ordinal)

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
