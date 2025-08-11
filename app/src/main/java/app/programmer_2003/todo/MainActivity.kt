package app.programmer_2003.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.programmer_2003.todo.home.HomeScreen
import app.programmer_2003.todo.task.TaskScreen
import app.programmer_2003.todo.task.TaskViewModel
import app.programmer_2003.todo.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ToDoTheme {
        val navController = rememberNavController()
        val taskViewModel: TaskViewModel = viewModel()
        Scaffold(
          modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
          NavHost(
            navController = navController,
            startDestination = "home"
          ) {
            composable("home") {
              HomeScreen(navController, taskViewModel)
            }
            composable("task") {
              TaskScreen(navController, taskViewModel)
            }
          }
        }
      }
    }
  }
}
