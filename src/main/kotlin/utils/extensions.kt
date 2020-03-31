package utils

import entities.TaskStatus
import entities.Task
import entities.ToDoList

fun String.toListFileName() = this + "_todo-list.json"

fun ToDoList.addTask(task: Task) {
    val maxId = tasks.maxBy { it.id }?.id
    task.id = maxId?.plus(1) ?: 0
    tasks.add(task)
}

fun ToDoList.setTaskAsDone(taskId: Int): Boolean {
    val temp = findTaskById(taskId)
    temp?.status = TaskStatus.DONE.code
    return temp != null
}

fun ToDoList.removeTask(taskId: Int): Boolean = tasks.remove(findTaskById(taskId))

fun ToDoList.todoTasks(): List<Task> {
    return tasks.filter { it.status == TaskStatus.TODO.code }
}

private fun ToDoList.findTaskById(taskId: Int): Task? = tasks.find { it.id == taskId }