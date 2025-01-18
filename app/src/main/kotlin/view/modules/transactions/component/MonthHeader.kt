package view.modules.transactions.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import view.shared.TextH1
import java.time.LocalDate
import java.time.Month

@Composable
fun MonthHeader(
    modifier: Modifier = Modifier,
    month: Month,
) {
    val monthNames = mapOf(
        Month.JANUARY to "Janeiro",
        Month.FEBRUARY to "Fevereiro",
        Month.MARCH to "Março",
        Month.APRIL to "Abril",
        Month.MAY to "Maio",
        Month.JUNE to "Junho",
        Month.JULY to "Julho",
        Month.AUGUST to "Agosto",
        Month.SEPTEMBER to "Setembro",
        Month.OCTOBER to "Outubro",
        Month.NOVEMBER to "Novembro",
        Month.DECEMBER to "Dezembro"
    )

    val formatedMonth = monthNames[month] ?: "Mês desconhecido"
    val monthTitle = if (month != LocalDate.now().month) formatedMonth else "Esse mês"
    val corners = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    var boxWidth by remember { mutableStateOf(0) }

    Box(
        modifier
            .offset(y = 0.5.dp)
            .padding(start = 90.dp)
    ) {
        Box(
            Modifier
            .clip(corners)
            .border(0.5.dp, MaterialTheme.colors.onSurface, corners)
            .onGloballyPositioned { boxWidth = it.size.width }
            .zIndex(1f)
        ) {
            TextH1(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                text = monthTitle
            )
        }
    }
}