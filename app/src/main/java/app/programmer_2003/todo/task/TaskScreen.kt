package app.programmer_2003.todo.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
  navController: NavController,
  taskViewModel: TaskViewModel
) {
  var taskText by remember { mutableStateOf("") }
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current

  LaunchedEffect(Unit) {
    taskText = taskViewModel.getSelectedTask()
    focusRequester.requestFocus()
    keyboardController?.show()
  }

  Scaffold(
    topBar = {
      TopAppBar(title = { Text("Task") })
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          if (taskViewModel.selectedTaskIndex != null) {
            taskViewModel.updateTask(taskText)
          } else {
            taskViewModel.addTask(taskText)
          }
          taskViewModel.setSelectedTask(null)
          navController.popBackStack()
        },
        modifier = Modifier.imePadding()
      ) {
        Icon(
          imageVector = Icons.Filled.Done,
          contentDescription = "Done"
        )
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      TextField(
        value = taskText,
        onValueChange = { taskText = it },
        placeholder = { Text("Enter your task") },
        modifier = Modifier
          .fillMaxSize()
          .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors(
          unfocusedContainerColor = MaterialTheme.colorScheme.background,
          focusedContainerColor = MaterialTheme.colorScheme.background
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
          keyboardController?.hide()
        })
      )
    }
  }
}
