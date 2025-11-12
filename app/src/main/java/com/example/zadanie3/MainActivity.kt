// MainActivity.kt

package pl.edu.pb.todoapp

import androidx.fragment.app.Fragment
import com.example.zadanie3.SingleFragmentActivity
import com.example.zadanie3.TaskFragment
import com.example.zadanie3.TaskListFragment
import java.util.UUID

class MainActivity : SingleFragmentActivity() {

    // Zmieniona implementacja createFragment (Punkt 33)
    override fun createFragment(): Fragment {
        // Pobranie ID zadania przekazanego przez Intent z TaskListFragment
        val taskId = intent.getSerializableExtra(TaskListFragment.KEY_EXTRA_TASK_ID) as? UUID
            ?: throw IllegalStateException("MainActivity started without a Task ID.")

        // Użycie metody newInstance do stworzenia TaskFragment z ID
        return TaskFragment.newInstance(taskId)
    }
}