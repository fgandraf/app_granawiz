package view.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.regular.SquaresFour
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.loading
import com.felipegandra.generated.resources.nav_dashboard
import org.jetbrains.compose.resources.stringResource
import view.modules.dashboard.component.*
import view.shared.AddressView
import view.shared.ClickableIcon
import view.shared.TextH2
import viewModel.DashboardViewModel

@Composable
fun DashboardScreen() {

    val viewModel = remember { DashboardViewModel() }
    val summary by viewModel.summary.collectAsState()
    val period by viewModel.period.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp, bottom = 15.dp)
            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
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
                    iconSize = DpSize(21.dp, 18.dp),
                    value = stringResource(Res.string.nav_dashboard),
                    rootPath = true,
                )
            }
            PeriodSelector(current = period, onSelect = { viewModel.selectPeriod(it) })
        }

        //===== BODY
        val current = summary
        if (current == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                TextH2(text = stringResource(Res.string.loading))
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            //---- Linha 1: Patrimônio + Fluxo + Ritmo
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NetWorthCard(modifier = Modifier.weight(1f), snapshot = current.netWorth)
                CashFlowCard(
                    modifier = Modifier.weight(1f),
                    cashFlow = current.cashFlow,
                    savingsRatePercent = current.savingsRatePercent,
                )
                SpendingPaceCard(modifier = Modifier.weight(1f), pace = current.spendingPace)
            }

            //---- Linha 2: Categorias + Evolução
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CategoryBreakdownCard(modifier = Modifier.weight(1f), breakdown = current.categoryBreakdown)
                MonthlyEvolutionCard(modifier = Modifier.weight(1f), data = current.monthlyEvolution)
            }

            //---- Linha 3: Cartões (largura inteira)
            CreditCardsCard(modifier = Modifier.fillMaxWidth(), snapshots = current.creditCards)

            //---- Linha 4: Top beneficiários + Top despesas + Lançamentos futuros
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TopPartiesCard(modifier = Modifier.weight(1f), parties = current.topParties)
                TopTransactionsCard(modifier = Modifier.weight(1f), transactions = current.topTransactions)
                UpcomingSchedulesCard(modifier = Modifier.weight(1f), occurrences = current.upcomingOccurrences)
            }


            Spacer(Modifier.height(8.dp))
        }
    }
}
