package view.modules.transactions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import utils.formatCurrency
import viewModel.UserPreferences
import view.shared.TextSmall

@Composable
fun TotalFooter(
    modifier: Modifier = Modifier,
    incomeBalance: Double,
    outcomeBalance: Double,
) {

    val corners = RoundedCornerShape(bottomEnd = 5.dp, bottomStart = 5.dp)

    Box(
        modifier.fillMaxWidth().offset(y = (-1).dp)
            .padding(end = 30.dp)
    ) {
        Box(
            Modifier
                .zIndex(1f)
                .align(Alignment.BottomEnd)
                .clip(corners)
                .border(0.5.dp, MaterialTheme.colors.onSurface, corners)
                .background(MaterialTheme.colors.background.copy(0.6f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                val currency = UserPreferences.currencySymbol
                TextSmall(
                    text = formatCurrency(incomeBalance, currency),
                    color = MaterialTheme.colors.onPrimary,
                )
                TextSmall(text = " - ", modifier = Modifier.padding(horizontal = 5.dp))
                TextSmall(
                    text = formatCurrency(outcomeBalance, currency),
                    color = MaterialTheme.colors.onError,
                )
                TextSmall(text = " = ", modifier = Modifier.padding(horizontal = 5.dp))
                val total: Double = incomeBalance - outcomeBalance
                TextSmall(
                    text = formatCurrency(total, currency),
                    color = if (total >= 0.0) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onError
                )
            }
        }
    }
}