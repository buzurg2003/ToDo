package app.programmer_2003.todo.home

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.programmer_2003.todo.task.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  navController: NavController,
  taskViewModel: TaskViewModel
) {
  val isSelectionMode = taskViewModel.isSelectionMode
  val selectedIndices = taskViewModel.selectedTaskIndices
  val context = LocalContext.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            if (isSelectionMode)
              "${selectedIndices.size} selected"
            else
              "Home"
          )
        },
        actions = {
          if (isSelectionMode) {
            IconButton(onClick = {
              taskViewModel.deleteSelectedTasks()
              Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }) {
              Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
          }
        }
      )
    },
    floatingActionButton = {
      if (!isSelectionMode) {
        FloatingActionButton(
          onClick = { navController.navigate("task") }
        ) {
          Icon(Icons.Filled.Add, contentDescription = "Add")
        }
      }
    }
  ) { paddingValues ->
    if (taskViewModel.tasks.isEmpty()) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("No items to show")
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
      ) {
        itemsIndexed(taskViewModel.tasks) { index, task ->
          val isSelected = index in selectedIndices

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(5.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .pointerInput(Unit) {
                  detectTapGestures(
                    onLongPress = {
                      taskViewModel.startSelection(index)
                    },
                    onTap = {
                      if (isSelectionMode) {
                        taskViewModel.toggleSelection(index)
                      } else {
                        taskViewModel.setSelectedTask(index)
                        navController.navigate("task")
                      }
                    }
                  )
                }
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = task,
                  style = MaterialTheme.typography.titleMedium,
                  modifier = Modifier.weight(1f)
                )

                Box(
                  modifier = Modifier.width(48.dp),
                  contentAlignment = Alignment.Center
                ) {
                  if (isSelectionMode) {
                    Checkbox(
                      checked = isSelected,
                      onCheckedChange = {
                        taskViewModel.toggleSelection(index)
                      }
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
