package view.modules.dashboard.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.ChartLineUp
import domain.structs.MonthlyFlow
import utils.formatCurrency
import view.modules.UserPreferences
import view.shared.TextSmall

@Composable
fun MonthlyEvolutionCard(
    modifier: Modifier = Modifier,
    data: List<MonthlyFlow>,
) {
    SummaryCard(
        modifier = modifier,
        title = "Evolução",
        icon = PhosphorIcons.Light.ChartLineUp,
        height = 290.dp,
    ) {
        if (data.isEmpty() || data.all { it.income == 0.0 && it.expense == 0.0 }) {
            TextSmall(text = "Sem dados suficientes", italic = true)
            return@SummaryCard
        }

        Legend()
        Spacer(Modifier.height(12.dp))

        val incomeColor = MaterialTheme.colors.onPrimary
        val expenseColor = MaterialTheme.colors.onError
        val netColor = MaterialTheme.colors.secondary
        val gridColor = MaterialTheme.colors.onSurface
        val textColor = MaterialTheme.colors.primary

        val maxValue = data.maxOf { maxOf(it.income, it.expense) }.coerceAtLeast(1.0)
        val allNets = data.map { it.net }
        val minNet = allNets.min().coerceAtMost(0.0)
        val maxNet = allNets.max().coerceAtLeast(0.0)
        val netRange = (maxNet - minNet).coerceAtLeast(1.0)

        Row(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            Canvas(modifier = Modifier.weight(1f).fillMaxHeight()) {
                val count = data.size
                val groupWidth = size.width / count
                val barsPadding = groupWidth * 0.15f
                val barWidth = (groupWidth - barsPadding * 3) / 2f
                val chartTop = 0f
                val chartBottom = size.height - 20f

                repeat(5) { i ->
                    val y = chartTop + (chartBottom - chartTop) * i / 4f
                    drawLine(
                        color = gridColor.copy(alpha = 0.4f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 0.5f,
                    )
                }

                data.forEachIndexed { index, flow ->
                    val groupStart = index * groupWidth + barsPadding
                    val incomeHeight = ((flow.income / maxValue) * (chartBottom - chartTop)).toFloat()
                    val expenseHeight = ((flow.expense / maxValue) * (chartBottom - chartTop)).toFloat()

                    drawRect(
                        color = incomeColor,
                        topLeft = Offset(groupStart, chartBottom - incomeHeight),
                        size = Size(barWidth, incomeHeight),
                    )
                    drawRect(
                        color = expenseColor,
                        topLeft = Offset(groupStart + barWidth + barsPadding, chartBottom - expenseHeight),
                        size = Size(barWidth, expenseHeight),
                    )
                }

                val path = Path()
                data.forEachIndexed { index, flow ->
                    val cx = index * groupWidth + groupWidth / 2f
                    val cy = chartBottom - (((flow.net - minNet) / netRange) * (chartBottom - chartTop)).toFloat()
                    if (index == 0) path.moveTo(cx, cy) else path.lineTo(cx, cy)
                }
                drawPath(path = path, color = netColor, style = Stroke(width = 2f))

                data.forEachIndexed { index, flow ->
                    val cx = index * groupWidth + groupWidth / 2f
                    val cy = chartBottom - (((flow.net - minNet) / netRange) * (chartBottom - chartTop)).toFloat()
                    drawCircle(color = netColor, radius = 3f, center = Offset(cx, cy))
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            data.forEach { flow ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    TextSmall(text = flow.label, color = textColor)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        val last = data.last()
        TextSmall(text = "Saldo de ${last.label}: ${formatCurrency(last.net, UserPreferences.currencySymbol)}")
    }
}

@Composable
private fun Legend() {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
        LegendDot(MaterialTheme.colors.onPrimary, "Receitas")
        LegendDot(MaterialTheme.colors.onError, "Despesas")
        LegendDot(MaterialTheme.colors.secondary, "Saldo")
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        TextSmall(text = label)
    }
}
