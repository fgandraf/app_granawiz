package view.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp

@Composable
fun FlowLayout(
    maxWidth: Dp,
    content: @Composable () -> Unit,
) {
    Layout(content = content) { measurables, constraints ->
        val maxWidthPx = maxWidth.toPx().toInt().coerceAtMost(constraints.maxWidth)
        var rowWidth = 0
        val rowHeights = mutableListOf<Int>()
        val rows = mutableListOf<List<Placeable>>()
        var currentRow = mutableListOf<Placeable>()

        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints)
            if (rowWidth + placeable.width > maxWidthPx && currentRow.isNotEmpty()) {
                // Adiciona a linha atual na lista de linhas e começa uma nova
                rows.add(currentRow)
                rowHeights.add(currentRow.maxOf { it.height })
                currentRow = mutableListOf()
                rowWidth = 0
            }
            currentRow.add(placeable)
            rowWidth += placeable.width
        }

        // Adiciona a última linha se contiver elementos
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
            rowHeights.add(currentRow.maxOf { it.height })
        }

        val height = rowHeights.sum().coerceAtMost(constraints.maxHeight)

        layout(width = maxWidthPx, height = height) {
            var yPosition = 0
            rows.forEachIndexed { index, row ->
                var xPosition = 0

                row.forEach { placeable ->
                    placeable.place(x = xPosition, y = yPosition)
                    xPosition += placeable.width
                }
                yPosition += rowHeights[index]
            }
        }
    }
}