package view.modules.dashboard.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.ArrowsLeftRight
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import domain.structs.CashFlow
import utils.formatCurrency
import view.modules.UserPreferences
import view.shared.TextH3
import view.shared.TextNormal
import view.shared.TextSmall

@Composable
fun CashFlowCard(
    modifier: Modifier = Modifier,
    cashFlow: CashFlow,
    savingsRatePercent: Double,
) {
    SummaryCard(
        modifier = modifier,
        title = stringResource(Res.string.dashboard_cash_flow_title),
        icon = PhosphorIcons.Light.ArrowsLeftRight,
        height = 150.dp
    ) {
        val currency = UserPreferences.currencySymbol
        RowLine(label = stringResource(Res.string.dashboard_income), value = formatCurrency(cashFlow.income, currency), valueColor = MaterialTheme.colors.onPrimary)
        Spacer(Modifier.height(6.dp))
        RowLine(label = stringResource(Res.string.dashboard_expenses), value = formatCurrency(cashFlow.expense, currency), valueColor = MaterialTheme.colors.onError)
        Spacer(Modifier.height(6.dp))
        val netColor = if (cashFlow.net >= 0) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onError
        RowLine(label = stringResource(Res.string.dashboard_balance), value = formatCurrency(cashFlow.net, currency), valueColor = netColor, bold = true)
        Spacer(Modifier.height(10.dp))
        TextSmall(text = "${stringResource(Res.string.dashboard_savings_rate)} ${String.format("%.1f%%", savingsRatePercent)}")
    }
}

@Composable
private fun RowLine(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color,
    bold: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextNormal(text = label)
        if (bold) TextH3(text = value, color = valueColor)
        else TextNormal(text = value, color = valueColor)
    }
}
