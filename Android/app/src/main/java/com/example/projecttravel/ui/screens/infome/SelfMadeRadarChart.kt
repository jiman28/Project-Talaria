package com.example.projecttravel.ui.screens.infome

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

// 직접 만든 방사형 그래프
@Composable
fun RadarChart(
    dataPoints: List<Float>,
    labels: List<String>,
) {
    Canvas(
        modifier = Modifier
            .size(250.dp)   // 여기서 Canvas 의 크기를 조정
            .aspectRatio(0.5f)   // 여기서 Canvas 내의 Content 의 크기를 조정
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.width * 0.4f
        val sides = dataPoints.size
        val angle = 360 / sides
        val maxValue = 1f // 최대 값을 조정

        // 데이터 값 표현
        for (index in 0 until sides) {
            val startAngle = Math.toRadians((angle * index).toDouble())
            val endAngle = Math.toRadians((angle * (index + 1)).toDouble())
            val startValue = dataPoints[index].coerceIn(0f, maxValue)
            val endValue = dataPoints[(index + 1) % sides].coerceIn(0f, maxValue)

            val startX = centerX + (startValue * radius * cos(startAngle)).toFloat()
            val startY = centerY + (startValue * radius * sin(startAngle)).toFloat()

            val endX = centerX + (endValue * radius * cos(endAngle)).toFloat()
            val endY = centerY + (endValue * radius * sin(endAngle)).toFloat()

            // 도형 그리기
            val path = androidx.compose.ui.graphics.Path()
            path.moveTo(centerX, centerY)
            path.lineTo(startX, startY)
            path.lineTo(endX, endY)
            path.close()

            // 도형 채우기
            drawPath(path, color = Color(0xFF87CEFA), style = androidx.compose.ui.graphics.drawscope.Fill)

            // 선 그리기
            drawLine(
                color = Color.Blue,
                start = Offset(centerX, centerY),
                end = Offset(startX, startY),
                strokeWidth = 3f
            )

            drawLine(
                color = Color.Blue,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 3f
            )

            // Optional: Add labels at each point
            val labelX = centerX + (radius * 1.25f * cos(startAngle)).toFloat()
            val labelY = centerY + (radius * 1.25f * sin(startAngle)).toFloat()
            val labelText = labels[index]
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    labelText,
                    labelX,
                    labelY,
                    Paint().apply {
                        color = Color.Black.toArgb()
                        textSize = 30f
                        textAlign = Paint.Align.CENTER
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                )
            }
        }

        // 그래프 눈금 그리기 (직선)
        val tickAngle = 360 / sides
        for (i in 0 until sides) {
            val tickAngle = Math.toRadians((tickAngle * i).toDouble())
            val tickX = centerX + (radius * cos(tickAngle)).toFloat()
            val tickY = centerY + (radius * sin(tickAngle)).toFloat()
            drawLine(
                color = Color.Gray,
                start = Offset(centerX, centerY),
                end = Offset(tickX, tickY),
                strokeWidth = 2f
            )
        }

        // 그래프 눈금 그리기 (거미줄)
        val webTicks = listOf(0.2f,0.4f,0.6f,0.8f,1.0f)
        for (tickIndex in webTicks.indices) {
            val spiderWeb = List(sides) { webTicks[tickIndex] }
            // 그래프 눈금 그리기 (육각형)
            for (index in 0 until sides) {
                val startAngle = Math.toRadians((angle * index).toDouble())
                val endAngle = Math.toRadians((angle * (index + 1)).toDouble())
                val startValue = spiderWeb[index].coerceIn(0f, maxValue)
                val endValue = spiderWeb[(index + 1) % sides].coerceIn(0f, maxValue)

                val startX = centerX + (startValue * radius * cos(startAngle)).toFloat()
                val startY = centerY + (startValue * radius * sin(startAngle)).toFloat()

                val endX = centerX + (endValue * radius * cos(endAngle)).toFloat()
                val endY = centerY + (endValue * radius * sin(endAngle)).toFloat()

                drawLine(
                    color = Color.Gray,
                    start = Offset(centerX, centerY),
                    end = Offset(startX, startY),
                    strokeWidth = 2f
                )

                drawLine(
                    color = Color.Gray,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 2f
                )
            }
        }
    }
}
