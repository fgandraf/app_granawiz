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
import com.adamglin.phosphoricons.light.Shapes
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import domain.structs.CategoryBreakdown
import utils.IconPaths
import utils.formatCurrency
import utils.rememberSvgPainter
import viewModel.UserPreferences
import view.shared.TextNormal
import view.shared.TextSmall

@Composable
fun CategoryBreakdownCard(
    modifier: Modifier = Modifier,
    breakdown: List<CategoryBreakdown>,
) {
    SummaryCard(
        modifier = modifier,
        title = stringResource(Res.string.dashboard_category_breakdown_title),
        icon = PhosphorIcons.Light.Shapes,
        height = 290.dp,
    ) {
        if (breakdown.isEmpty()) {
            TextSmall(text = stringResource(Res.string.dashboard_no_category_expenses), italic = true)
            return@SummaryCard
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            breakdown.forEach { row -> CategoryRow(row) }
        }
    }
}

@Composable
private fun CategoryRow(row: CategoryBreakdown) {
    val fraction = (row.percent / 100.0).toFloat().coerceIn(0f, 1f)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (row.category.icon.isNotEmpty()) {
                    Icon(
                        painter = rememberSvgPainter(IconPaths.CATEGORY_PACK + row.category.icon),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(14.dp),
                    )
                }
                TextNormal(text = row.category.name)
            }
            TextNormal(
                text = "${formatCurrency(row.amount, UserPreferences.currencySymbol)} (${String.format("%.0f%%", row.percent)})",
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colors.secondaryVariant),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .background(MaterialTheme.colors.onError),
            )
        }
    }
}
