package view.modules.transactions.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import utils.brMoney
import view.shared.TextSmall

@Composable
fun TotalFooter(
    modifier: Modifier = Modifier,
    incomeBalance: Double,
    outcomeBalance: Double,
) {

    val corners = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)
    var boxWidth by remember { mutableStateOf(0) }

    Box(
        modifier.fillMaxWidth().offset(y = (-1).dp)
            .padding(end = 90.dp)
    ) {
        Box(
            Modifier
                .zIndex(1f)
                .align(Alignment.BottomEnd)
                .clip(corners)
                .border(0.5.dp, MaterialTheme.colors.onSurface, corners)
                .onGloballyPositioned { boxWidth = it.size.width }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextSmall(
                    text = brMoney.format(incomeBalance),
                    color = MaterialTheme.colors.onPrimary,
                )
                TextSmall(text = " - ", modifier = Modifier.padding(horizontal = 5.dp))
                TextSmall(
                    text = brMoney.format(outcomeBalance),
                    color = MaterialTheme.colors.onError,
                )
                TextSmall(text = " = ", modifier = Modifier.padding(horizontal = 5.dp))
                val total: Double = incomeBalance - outcomeBalance
                TextSmall(
                    text = brMoney.format(total),
                    color = if (total >= 0.0) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onError
                )
            }
        }
    }
}