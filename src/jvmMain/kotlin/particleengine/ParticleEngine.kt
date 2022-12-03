package particleengine

import Dimension
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.random.Random

data class Particle(
    var posX: Float = 0f, var posY: Float = 0f,
    var moveX: Float = 0f, var moveY: Float = 0f,
    var r: Int = 0, var g: Int = 0, var b: Int = 0,
    var size: Float
)

private const val NS_PER_SEC = 1000000000f

class ParticleEngine(private val viewportSize: Dimension) {
    var running by mutableStateOf(false)
    private var particles by mutableStateOf(List(100) { Particle(size = 1f) })

    // FPS calculations
    // Sum of all rendered frames
    private var frameSum by mutableStateOf(0)
    private var lastTimeOfFPSRead = 0L
    private var lastFrameSum = 0
    private var lastTick: Long = 0

    @Composable
    fun ParticleEngineView() {
        LaunchedEffect(Unit) {
            while (true) {
                withFrameNanos {
                    update(it)
                }
            }
        }
        Canvas(
            modifier = Modifier
                .size(viewportSize.width.dp, viewportSize.height.dp)
        ) {
            if (!running) return@Canvas
            val unusedVariable = frameSum
            for (particle in particles) {
                with(particle) {
                    drawCircle(
                        color = Color(r, g, b),
                        radius = size,
                        center = Offset(posX, posY + 10)
                    )
                }
            }
        }
    }

    fun start() {
        println("Starting engine!")
        lastTick = System.nanoTime()
        for (particle in particles) {
            val ang = Random.nextDouble() * PI * 2
            val vel = 15 + Random.nextDouble() * 100
            with(particle) {
                posX = (Math.random() * viewportSize.width).toFloat()
                posY = Random.nextFloat() * viewportSize.height
                moveX = Random.nextFloat() * 6 - 3
                moveY = Random.nextFloat() * 50
//                posY = (Math.random() * viewportSize.height).toFloat();
//                moveX = (cos(ang) * vel).toFloat()
//                moveY = (sin(ang) * vel).toFloat()

                val color = Color.hsv(Random.nextFloat() * 360, 1f, 1f)
                r = (color.red * 255).toInt()
                g = (color.green * 255).toInt()
                b = (color.blue * 255).toInt()

                size = Random.nextFloat() * 3 + 2f
            }
        }

        this.running = true
    }

    fun stop() {
        println("Stopping engine!")
        this.running = false
    }

    fun getFPSSinceLastCall(): Int {
        val currentTime = System.nanoTime()

        val renderedFrames = frameSum - lastFrameSum
        val timeDiff = currentTime - lastTimeOfFPSRead
        val fps = renderedFrames / (timeDiff / NS_PER_SEC)

        lastTimeOfFPSRead = currentTime
        lastFrameSum = frameSum
        return fps.toInt();
    }

    private fun update(currentTickNano: Long) {
        val tickDiff = (currentTickNano - lastTick).coerceAtLeast(1)
        lastTick = currentTickNano
        var factor = tickDiff / NS_PER_SEC
        if (factor > 1) {
            println("Simulation lagging behind realtime.")
            factor = 1f
        }

        if (!running) return

        for (particle in particles) {
            with(particle) {
                moveY += (Random.nextFloat() * 5 + 5) * factor
                // drag
                moveY -= moveY * 0.3f * factor

                moveX += (Random.nextFloat() * 100 - 50) * factor
                moveX -= moveX * 0.2f * factor

                posX += moveX * factor
                posY += moveY * factor
                if (posX >= viewportSize.width) {
                    posX = viewportSize.width - (posX - viewportSize.width)
                    moveX = -moveX.absoluteValue
                } else if (posX <= 0) {
                    posX = posX.absoluteValue
                    moveX = moveX.absoluteValue
                }
                if (posY >= viewportSize.height) {
                    // bottom
                    posY -= viewportSize.height
                    moveY = Random.nextFloat() * 50
                } else if (posY <= 0) {
                    posY = posY.absoluteValue
                    moveY = moveY.absoluteValue
                }
            }
        }
        frameSum++
    }
}
