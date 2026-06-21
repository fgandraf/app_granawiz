package view.modules.transactions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import view.shared.TextH1
import java.time.Month
import java.time.YearMonth

@Composable
fun MonthHeader(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
) {
    val monthNames = mapOf(
        Month.JANUARY to stringResource(Res.string.month_january),
        Month.FEBRUARY to stringResource(Res.string.month_february),
        Month.MARCH to stringResource(Res.string.month_march),
        Month.APRIL to stringResource(Res.string.month_april),
        Month.MAY to stringResource(Res.string.month_may),
        Month.JUNE to stringResource(Res.string.month_june),
        Month.JULY to stringResource(Res.string.month_july),
        Month.AUGUST to stringResource(Res.string.month_august),
        Month.SEPTEMBER to stringResource(Res.string.month_september),
        Month.OCTOBER to stringResource(Res.string.month_october),
        Month.NOVEMBER to stringResource(Res.string.month_november),
        Month.DECEMBER to stringResource(Res.string.month_december),
    )

    val now = YearMonth.now()
    val monthName = monthNames[yearMonth.month] ?: stringResource(Res.string.month_unknown)
    val monthTitle = when {
        yearMonth == now -> stringResource(Res.string.month_current)
        yearMonth.year != now.year -> "$monthName ${yearMonth.year}"
        else -> monthName
    }
    val corners = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)

    Box(
        modifier
            .offset(y = 0.5.dp)
            .padding(start = 80.dp)
    ) {
        Box(
            Modifier
            .clip(corners)
            .border(0.5.dp, MaterialTheme.colors.onSurface, corners)
            .background(MaterialTheme.colors.background.copy(0.6f))
            .zIndex(1f)
        ) {
            TextH1(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                text = monthTitle
            )
        }
    }
}