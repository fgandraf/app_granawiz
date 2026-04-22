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
import core.structs.NetWorthSnapshot
import utils.brMoney
import view.shared.TextH1
import view.shared.TextNormal
import view.shared.TextSmall
import androidx.compose.material.Icon

@Composable
fun NetWorthCard(
    modifier: Modifier = Modifier,
    snapshot: NetWorthSnapshot,
) {
    SummaryCard(modifier = modifier, title = "Patrimônio total", icon = PhosphorIcons.Light.Wallet, height = 150.dp) {
        TextH1(text = brMoney.format(snapshot.total))
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
                text = "$sign${brMoney.format(snapshot.deltaAmount)} ($sign$percentStr)",
                color = color,
            )
        }
        Spacer(Modifier.height(4.dp))
        TextSmall(text = "variação no mês")
    }
}
