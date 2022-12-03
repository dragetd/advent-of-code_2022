import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import particleengine.ParticleEngine
import java.time.LocalDateTime

data class Dimension(val width: Int = 1, val height: Int = 1)

@Composable
fun AOC2022() {
    val viewportSize = Dimension(790, 450)
    val particleEngine = remember { ParticleEngine(viewportSize) }
    var fps by mutableStateOf(0)
//    val density = LocalDensity.current


    var input by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("${currentAoCDay()}a") }
    var result by remember { mutableStateOf("") }

    Scaffold(
        backgroundColor = Color.LightGray,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "AoC 2022! ${if (particleEngine.running) "Snowing!" else "Start snow! -> "}",
                    fontSize = 50.sp,
                    maxLines = 1
                )
                Button(
                    onClick = {
                        if (particleEngine.running) {
                            particleEngine.stop()
                            particleEngine.running = false
                        } else {
                            particleEngine.start()
                        }
                    }
                ) {
                    Text(" ${if (particleEngine.running) "Stop" else "Start"}")
                }
            }
        }, content = {
            particleEngine.ParticleEngineView()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.background(Color.White)
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.2f),
                    placeholder = { Text("Please enter the puzzle input…") },
                    label = { Text(text = "Aoc 2022 Input") },
                    leadingIcon = { Icon(Icons.Default.List, "Input") }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Solution for day: ")
                    TextField(
                        value = day,
                        onValueChange = { day = it }
                    )
                    Button(onClick = { result = solution(day, input) }) {
                        Icon(Icons.Filled.PlayArrow, "Solve")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp).background(Color.White),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Result is: ")
                    SelectionContainer(
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(result)
                    }
                }
            }
        },
        bottomBar = {
            LaunchedEffect(Unit) {
                while (true) {
                    delay(1000)
                    fps = particleEngine.getFPSSinceLastCall()
                }
            }
            Text("FPS: $fps")
        }
    )
}

fun solution(task: String, input: String): String {
    try {
        // TODO: Ugly Java Reflection to get the method
        val day = String.format("%02d", task.replace("[^0-9]".toRegex(), "").toInt())
        val solveFunction = Class.forName("solution.Day${day}Kt").methods.first { it.name == "solve$task" }
        return solveFunction.invoke(null, input.trim()).toString()
    } catch (ex: NoSuchElementException) {
        return "This solution for this day is not yet implemented."
    } catch (ex: ClassNotFoundException) {
        return "This day is not yet implemented."
    } catch (ex: Exception) {
        return when (ex.cause) {
            is NumberFormatException -> "Error: Solution could not parse a numer."
            is NullPointerException -> "Error: Solution returned null."
            else -> "Error: Could not solve. ${ex.message}"
        }
    }
}

fun currentAoCDay(): Int {
    val now = LocalDateTime.now()
    return if (now.monthValue != 12) 1 else now.dayOfMonth
}
