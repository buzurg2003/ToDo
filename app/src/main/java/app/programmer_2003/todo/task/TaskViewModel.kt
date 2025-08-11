package app.programmer_2003.todo.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
  val tasks = mutableStateListOf<String>()

  // ! For multi-selection
  val selectedTaskIndices = mutableStateListOf<Int>()
  var isSelectionMode by mutableStateOf(false)
    private set

  // ! For single task editing
  var selectedTaskIndex: Int? = null
    private set

  fun addTask(task: String) {
    if (task.isNotBlank()) tasks.add(task)
  }

  fun updateTask(updatedTask: String) {
    selectedTaskIndex?.let { index ->
      if (updatedTask.isNotBlank() && index in tasks.indices) {
        tasks[index] = updatedTask
      }
    }
  }

  fun deleteSelectedTasks() {
    selectedTaskIndices.sortedDescending().forEach {
      if (it in tasks.indices) tasks.removeAt(it)
    }
    clearSelection()
  }

  fun toggleSelection(index: Int) {
    if (index in selectedTaskIndices) {
      selectedTaskIndices.remove(index)
    } else {
      selectedTaskIndices.add(index)
    }

    if (selectedTaskIndices.isEmpty()) {
      isSelectionMode = false
    }
  }

  fun startSelection(index: Int) {
    isSelectionMode = true
    selectedTaskIndices.clear()
    selectedTaskIndices.add(index)
  }

  fun clearSelection() {
    isSelectionMode = false
    selectedTaskIndices.clear()
  }

  fun setSelectedTask(index: Int?) {
    selectedTaskIndex = index
  }

  fun getSelectedTask(): String {
    return selectedTaskIndex?.let { tasks.getOrNull(it) ?: "" } ?: ""
  }
}
