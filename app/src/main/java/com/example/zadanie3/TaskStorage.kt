import com.example.zadanie3.models.Task
import java.util.UUID

object TaskStorage {

    private val tasks: MutableList<Task> = mutableListOf()

    init {
       val taskCount = 150
        for (i in 1 until taskCount) {
            val task = Task(
                name = "Pilne zadanie numer $i",
                isDone = i % 3 == 0
            )
            tasks.add(task)
        }
    }

    fun getTasks(): List<Task> {
        return tasks
    }

    fun getTask(id: UUID): Task? {
        return tasks.find { it.id.toString() == id.toString() }
    }
}
