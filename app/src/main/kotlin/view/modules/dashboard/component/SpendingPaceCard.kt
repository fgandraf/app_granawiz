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
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
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
    SummaryCard(modifier = modifier, title = stringResource(Res.string.dashboard_spending_pace_title), icon = PhosphorIcons.Light.Gauge, height = 150.dp) {
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
                    TextSmall(text = stringResource(Res.string.dashboard_spending_pace_unavailable))
                }
            }
            return@SummaryCard
        }

        val lengthOfMonth = YearMonth.from(pace.today).lengthOfMonth()
        TextNormal(text = stringResource(Res.string.dashboard_spending_pace_day, pace.today.dayOfMonth, lengthOfMonth))
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
        TextH3(text = "${String.format("%.0f%%", pace.percentOfAverage)} ${stringResource(Res.string.dashboard_spending_pace_above_average)}", color = color)
        Spacer(Modifier.height(4.dp))
        val currency = UserPreferences.currencySymbol
        TextSmall(
            text = "${formatCurrency(pace.monthTotalSoFar, currency)} ${stringResource(Res.string.dashboard_spending_pace_detail)} ${formatCurrency(pace.averageAtSameDay, currency)}"
        )
    }
}
