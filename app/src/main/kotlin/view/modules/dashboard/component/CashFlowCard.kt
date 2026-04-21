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
import core.structs.CashFlow
import utils.brMoney
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
        title = "Fluxo do período",
        icon = PhosphorIcons.Light.ArrowsLeftRight,
    ) {
        RowLine(label = "Receitas", value = brMoney.format(cashFlow.income), valueColor = MaterialTheme.colors.onPrimary)
        Spacer(Modifier.height(6.dp))
        RowLine(label = "Despesas", value = brMoney.format(cashFlow.expense), valueColor = MaterialTheme.colors.onError)
        Spacer(Modifier.height(6.dp))
        val netColor = if (cashFlow.net >= 0) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onError
        RowLine(label = "Saldo", value = brMoney.format(cashFlow.net), valueColor = netColor, bold = true)
        Spacer(Modifier.height(10.dp))
        TextSmall(text = "Taxa de poupança: ${String.format("%.1f%%", savingsRatePercent)}")
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
