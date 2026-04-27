package view.modules.dashboard.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CurrencyCircleDollar
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import domain.entity.Transaction
import utils.formatCurrency
import kotlin.math.abs
import view.modules.UserPreferences
import view.shared.TextNormal
import view.shared.TextSmall
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TopTransactionsCard(
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
) {
    SummaryCard(
        modifier = modifier,
        title = stringResource(Res.string.dashboard_top_expenses_title),
        icon = PhosphorIcons.Light.CurrencyCircleDollar,
        height = 240.dp
    ) {
        if (transactions.isEmpty()) {
            TextSmall(text = stringResource(Res.string.dashboard_no_category_expenses), italic = true)
            return@SummaryCard
        }

        val ptBr = Locale.of("pt", "BR")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            transactions.forEach { tx ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        TextNormal(text = tx.party.name)
                        Spacer(Modifier.height(2.dp))
                        val day = tx.date.dayOfMonth
                        val month = tx.date.month.getDisplayName(TextStyle.SHORT, ptBr)
                        TextSmall(text = "$day $month · ${tx.category.name}")
                    }
                    TextNormal(text = formatCurrency(abs(tx.balance), UserPreferences.currencySymbol), color = MaterialTheme.colors.onError)
                }
            }
        }
    }
}
