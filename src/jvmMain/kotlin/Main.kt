
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "Advent of code 2022",
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(800.dp, 600.dp)
        )
    ) {
        AOC2022()
    }
}
