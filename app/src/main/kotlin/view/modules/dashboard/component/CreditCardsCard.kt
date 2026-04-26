package view.modules.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CreditCard
import domain.structs.CreditCardSnapshot
import utils.formatCurrency
import view.modules.UserPreferences
import view.shared.TextNormal
import view.shared.TextSmall

@Composable
fun CreditCardsCard(
    modifier: Modifier = Modifier,
    snapshots: List<CreditCardSnapshot>,
) {
    SummaryCard(modifier = modifier, title = "Cartões de crédito", icon = PhosphorIcons.Light.CreditCard) {
        if (snapshots.isEmpty()) {
            TextSmall(text = "Nenhum cartão cadastrado", italic = true)
            return@SummaryCard
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            snapshots.forEach { snap -> CreditCardRow(snap) }
        }
    }
}

@Composable
private fun CreditCardRow(snap: CreditCardSnapshot) {
    val limit = snap.account.creditLimit
    val usedFraction = if (limit > 0.0) (snap.currentInvoice / limit).coerceIn(0.0, 1.0).toFloat() else 0f
    val color = when {
        usedFraction < 0.6f -> MaterialTheme.colors.onPrimary
        usedFraction < 0.85f -> MaterialTheme.colors.secondary
        else -> MaterialTheme.colors.onError
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextNormal(text = snap.account.name)
            val currency = UserPreferences.currencySymbol
            TextNormal(
                text = "${formatCurrency(snap.currentInvoice, currency)} / ${formatCurrency(limit, currency)}",
                color = color,
            )
        }
        Spacer(Modifier.height(6.dp))
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
                    .fillMaxWidth(usedFraction)
                    .background(color),
            )
        }
        Spacer(Modifier.height(4.dp))
        val dueLabel = when {
            snap.daysToDue == 0L -> "vence hoje"
            snap.daysToDue == 1L -> "vence amanhã"
            snap.daysToDue > 0 -> "vence em ${snap.daysToDue} dias"
            else -> "venceu há ${-snap.daysToDue} dias"
        }
        TextSmall(text = "Disponível: ${formatCurrency(snap.availableLimit, UserPreferences.currencySymbol)} · $dueLabel")
    }
}
