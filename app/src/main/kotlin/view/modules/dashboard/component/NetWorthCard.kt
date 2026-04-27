package view.modules.dashboard.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.TrendDown
import com.adamglin.phosphoricons.light.TrendUp
import com.adamglin.phosphoricons.light.Wallet
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import domain.structs.NetWorthSnapshot
import utils.formatCurrency
import view.modules.UserPreferences
import view.shared.TextH1
import view.shared.TextNormal
import view.shared.TextSmall
import androidx.compose.material.Icon

@Composable
fun NetWorthCard(
    modifier: Modifier = Modifier,
    snapshot: NetWorthSnapshot,
) {
    SummaryCard(modifier = modifier, title = stringResource(Res.string.dashboard_net_worth_title), icon = PhosphorIcons.Light.Wallet, height = 150.dp) {
        val currency = UserPreferences.currencySymbol
        TextH1(text = formatCurrency(snapshot.total, currency))
        Spacer(Modifier.height(8.dp))

        val positive = snapshot.deltaAmount >= 0
        val color = if (positive) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onError
        val trendIcon = if (positive) PhosphorIcons.Light.TrendUp else PhosphorIcons.Light.TrendDown
        val sign = if (positive) "+" else ""
        val percentStr = String.format("%.1f%%", snapshot.deltaPercent)

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(
                imageVector = trendIcon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp),
            )
            TextNormal(
                text = "$sign${formatCurrency(snapshot.deltaAmount, currency)} ($sign$percentStr)",
                color = color,
            )
        }
        Spacer(Modifier.height(4.dp))
        TextSmall(text = stringResource(Res.string.dashboard_net_worth_variation))
    }
}
