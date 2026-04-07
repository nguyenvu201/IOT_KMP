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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SensorLineChart(
    data: List<Double>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    
    val maxDataRaw = data.maxOrNull() ?: 1.0
    val minDataRaw = data.minOrNull() ?: 0.0
    
    // Antijitter: Add 10% padding to bounds and enforce min range of 5 units to avoid bouncy scale
    val padding = (maxDataRaw - minDataRaw) * 0.1
    val paddedMin = minDataRaw - padding
    val paddedMax = maxDataRaw + padding
    
    val minData = if (paddedMax - paddedMin < 5) paddedMin - 2.5 else paddedMin
    val maxData = if (paddedMax - paddedMin < 5) paddedMax + 2.5 else paddedMax
    val range = maxData - minData

    val textMeasurer = rememberTextMeasurer()

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

        val lastVal = data.last()
        drawText(
            textMeasurer = textMeasurer,
            text = lastVal.toString().take(5),
            topLeft = Offset(width - 40.dp.toPx(), ((1.0f - ((lastVal - minData) / range).toFloat()) * height) - 20.dp.toPx()),
            style = TextStyle(color = lineColor, fontSize = 14.sp)
        )
        
        drawText(
            textMeasurer = textMeasurer,
            text = "Max: ${maxDataRaw.toString().take(5)}",
            topLeft = Offset(0f, 0f),
            style = TextStyle(color = Color.Gray, fontSize = 10.sp)
        )
        drawText(
            textMeasurer = textMeasurer,
            text = "Min: ${minDataRaw.toString().take(5)}",
            topLeft = Offset(0f, height - 15.dp.toPx()),
            style = TextStyle(color = Color.Gray, fontSize = 10.sp)
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
    
    val maxDataRaw = data.maxOrNull() ?: 1.0
    // Fix bouncy jitter on bar chart by forcing top-padding
    val maxData = if (maxDataRaw < 5) 5.0 else maxDataRaw * 1.2
    val minData = 0.0 
    val range = maxData - minData

    val textMeasurer = rememberTextMeasurer()

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

        val lastVal = data.last()
        val lastIndex = data.size - 1
        val lastNormalizedY = ((lastVal - minData) / range).toFloat().coerceIn(0f, 1f)
        val lastBarHeight = lastNormalizedY * height
        val lastX = lastIndex * (barWidth + spacing) + spacing / 2
        val lastY = height - lastBarHeight

        drawText(
            textMeasurer = textMeasurer,
            text = lastVal.toString().take(5),
            topLeft = Offset(lastX - 10.dp.toPx(), lastY - 20.dp.toPx()),
            style = TextStyle(color = barColor, fontSize = 14.sp)
        )
        
        drawText(
            textMeasurer = textMeasurer,
            text = "Max: ${maxDataRaw.toString().take(5)}",
            topLeft = Offset(0f, 0f),
            style = TextStyle(color = Color.Gray, fontSize = 10.sp)
        )
    }
}
