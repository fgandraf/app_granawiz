package view.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.regular.SquaresFour
import view.modules.dashboard.component.*
import view.shared.AddressView
import view.shared.ClickableIcon
import view.shared.TextH2
import viewModel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = DashboardViewModel()) {

    val summary by viewModel.summary.collectAsState()
    val period by viewModel.period.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        //===== HEADER
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(
                    enabled = false,
                    icon = PhosphorIcons.Bold.ArrowLeft,
                    iconSize = 22.dp,
                    boxSize = 25.dp,
                ) { }
                Spacer(Modifier.width(10.dp))
                AddressView(
                    icon = PhosphorIcons.Regular.SquaresFour,
                    value = "Dashboard",
                    rootPath = true,
                )
            }
            PeriodSelector(current = period, onSelect = { viewModel.selectPeriod(it) })
        }

        //===== BODY
        val current = summary
        if (current == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                TextH2(text = "Carregando...")
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            //---- Linha 1: Patrimônio + Fluxo + Ritmo
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NetWorthCard(modifier = Modifier.weight(1f), snapshot = current.netWorth)
                CashFlowCard(
                    modifier = Modifier.weight(1f),
                    cashFlow = current.cashFlow,
                    savingsRatePercent = current.savingsRatePercent,
                )
                if (current.spendingPace != null) {
                    SpendingPaceCard(modifier = Modifier.weight(1f), pace = current.spendingPace)
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            //---- Linha 2: Cartões (largura inteira)
            CreditCardsCard(modifier = Modifier.fillMaxWidth(), snapshots = current.creditCards)

            //---- Linha 3: Categorias + Evolução
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CategoryBreakdownCard(modifier = Modifier.weight(1f), breakdown = current.categoryBreakdown)
                MonthlyEvolutionCard(modifier = Modifier.weight(1f), data = current.monthlyEvolution)
            }

            //---- Linha 4: Top beneficiários + Top despesas
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TopPartiesCard(modifier = Modifier.weight(1f), parties = current.topParties)
                TopTransactionsCard(modifier = Modifier.weight(1f), transactions = current.topTransactions)
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
