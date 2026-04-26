package view.modules.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Gauge
import domain.structs.SpendingPace
import utils.formatCurrency
import view.modules.UserPreferences
import view.shared.TextH3
import view.shared.TextNormal
import view.shared.TextSmall
import java.time.YearMonth

@Composable
fun SpendingPaceCard(
    modifier: Modifier = Modifier,
    pace: SpendingPace?,
) {
    SummaryCard(modifier = modifier, title = "Ritmo do mês", icon = PhosphorIcons.Light.Gauge, height = 150.dp) {
        if (pace == null) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = PhosphorIcons.Light.Gauge,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
                        modifier = Modifier.size(32.dp),
                    )
                    Spacer(Modifier.height(8.dp))
                    TextSmall(text = "Disponível após 3 meses de registros")
                }
            }
            return@SummaryCard
        }

        val lengthOfMonth = YearMonth.from(pace.today).lengthOfMonth()
        TextNormal(text = "Dia ${pace.today.dayOfMonth} de $lengthOfMonth")
        Spacer(Modifier.height(8.dp))

        val percent = pace.percentOfAverage.coerceIn(0.0, 200.0)
        val fraction = (percent / 150.0).coerceAtMost(1.0).toFloat()
        val color = when {
            pace.percentOfAverage <= 80.0 -> MaterialTheme.colors.onPrimary
            pace.percentOfAverage <= 110.0 -> MaterialTheme.colors.secondary
            else -> MaterialTheme.colors.onError
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.secondaryVariant),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .background(color),
            )
        }
        Spacer(Modifier.height(8.dp))
        TextH3(text = "${String.format("%.0f%%", pace.percentOfAverage)} da média", color = color)
        Spacer(Modifier.height(4.dp))
        val currency = UserPreferences.currencySymbol
        TextSmall(
            text = "${formatCurrency(pace.monthTotalSoFar, currency)} gastos · média ${formatCurrency(pace.averageAtSameDay, currency)}"
        )
    }
}
