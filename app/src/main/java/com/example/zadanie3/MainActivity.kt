package com.example.zadanie3

import androidx.fragment.app.Fragment
import java.util.UUID

class MainActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        val taskId = intent.getSerializableExtra(TaskListFragment.KEY_EXTRA_TASK_ID) as? UUID
            ?: throw IllegalStateException("MainActivity started without a Task ID.")

        return TaskFragment.newInstance(taskId)
    }
}
