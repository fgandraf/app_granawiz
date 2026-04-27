package view.modules.dashboard.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.HandArrowUp
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import domain.structs.PartyVolume
import utils.formatCurrency
import view.modules.UserPreferences
import view.shared.TextNormal
import view.shared.TextSmall

@Composable
fun TopPartiesCard(
    modifier: Modifier = Modifier,
    parties: List<PartyVolume>,
) {
    SummaryCard(modifier = modifier, title = stringResource(Res.string.dashboard_top_parties_title), icon = PhosphorIcons.Light.HandArrowUp, height = 170.dp) {
        if (parties.isEmpty()) {
            TextSmall(text = stringResource(Res.string.dashboard_no_data_in_period), italic = true)
            return@SummaryCard
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            parties.forEach { pv ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        TextNormal(text = pv.party.name)
                        Spacer(Modifier.height(2.dp))
                        TextSmall(
                            text = "${pv.transactionCount} ${if (pv.transactionCount == 1) stringResource(Res.string.dashboard_transaction) else stringResource(Res.string.dashboard_transactions)}",
                        )
                    }
                    TextNormal(text = formatCurrency(pv.amount, UserPreferences.currencySymbol))
                }
            }
        }
    }
}
