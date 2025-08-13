package app.programmer_2003.todo.task

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
  private val taskDataStore = TaskDataStore(application.applicationContext)

  val tasks = mutableStateListOf<String>()
  val selectedTaskIndices = mutableStateListOf<Int>()
  var isSelectionMode by mutableStateOf(false)
    private set

  var selectedTaskIndex: Int? = null
    private set

  init {
    viewModelScope.launch {
      taskDataStore.tasksFlow.collectLatest { loadedTasks ->
        tasks.clear()
        tasks.addAll(loadedTasks)
      }
    }
  }

  private fun save() {
    viewModelScope.launch {
      taskDataStore.saveTasks(tasks)
    }
  }

  fun addTask(task: String) {
    if (task.isNotBlank()) {
      tasks.add(task)
      save()
    }
  }

  fun updateTask(updatedTask: String) {
    selectedTaskIndex?.let { index ->
      if (updatedTask.isNotBlank() && index in tasks.indices) {
        tasks[index] = updatedTask
        save()
      }
    }
  }

  fun deleteSelectedTasks() {
    selectedTaskIndices.sortedDescending().forEach {
      if (it in tasks.indices) tasks.removeAt(it)
    }
    clearSelection()
    save()
  }

  fun toggleSelection(index: Int) {
    if (index in selectedTaskIndices) {
      selectedTaskIndices.remove(index)
    } else {
      selectedTaskIndices.add(index)
    }
    isSelectionMode = selectedTaskIndices.isNotEmpty()
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
