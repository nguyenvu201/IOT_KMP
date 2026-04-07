package caonguyen.vu.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SensorLineChart(
    data: List<Double>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    
    val maxData = data.maxOrNull() ?: 1.0
    val minData = data.minOrNull() ?: 0.0
    val range = if (maxData == minData) 1.0 else (maxData - minData)

    Canvas(modifier = modifier.fillMaxSize().padding(16.dp)) {
        val width = size.width
        val height = size.height
        val stepX = width / (if (data.size > 1) data.size - 1 else 1).toFloat()
        
        val path = Path()
        
        data.forEachIndexed { index, value ->
            val normalizedY = 1.0f - ((value - minData) / range).toFloat()
            val x = index * stepX
            val y = normalizedY * height
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 4.dp.toPx())
        )
    }
}

@Composable
fun SensorBarChart(
    data: List<Double>,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    
    val maxData = data.maxOrNull() ?: 1.0
    val minData = 0.0 
    val range = if (maxData == minData) 1.0 else (maxData - minData)

    Canvas(modifier = modifier.fillMaxSize().padding(16.dp)) {
        val width = size.width
        val height = size.height
        val barWidth = width / (data.size * 2).toFloat()
        val spacing = barWidth
        
        data.forEachIndexed { index, value ->
            val normalizedY = ((value - minData) / range).toFloat().coerceIn(0f, 1f)
            val barHeight = normalizedY * height
            val x = index * (barWidth + spacing) + spacing / 2
            val y = height - barHeight
            
            drawRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )
        }
    }
}
