import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import particleengine.ParticleEngine

data class Dimension(val width: Int = 1, val height: Int = 1)

@Composable
fun AOC2022() {
    val viewportSize = Dimension(790, 450)
    val particleEngine = remember { ParticleEngine(viewportSize) }
    var fps by mutableStateOf(0)
//    val density = LocalDensity.current

    Scaffold(
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
    ) {
        particleEngine.ParticleEngineView()
    }
}

