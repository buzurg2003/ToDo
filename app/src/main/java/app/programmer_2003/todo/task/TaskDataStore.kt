package app.programmer_2003.todo.task

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("task_prefs")

class TaskDataStore(private val context: Context) {
  companion object {
    private val TASKS_KEY = stringSetPreferencesKey("tasks")
  }

  val tasksFlow: Flow<List<String>> = context.dataStore.data
    .map { preferences ->
      preferences[TASKS_KEY]?.toList() ?: emptyList()
    }

  suspend fun saveTasks(tasks: List<String>) {
    context.dataStore.edit { preferences ->
      preferences[TASKS_KEY] = tasks.toSet()
    }
  }
}
