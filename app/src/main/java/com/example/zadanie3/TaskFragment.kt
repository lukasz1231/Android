package com.example.zadanie3

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zadanie3.models.Task

class TaskFragment: Fragment() {
    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.task = Task()

        val nameField = view.findViewById<android.widget.TextView>(R.id.task_name)
        nameField.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.name = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        val dateButton = view.findViewById<android.widget.Button>(R.id.task_date)
        dateButton.setText(task.date.toString())
        dateButton.isEnabled = false


        val doneCheckBox = view.findViewById<android.widget.CheckBox>(R.id.task_done)
        doneCheckBox.isChecked = task.isDone
        doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked
        }
    }

    }
