package view.modules.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CalendarBlank
import core.enums.TransactionType
import domain.schedule.usecases.ScheduleOccurrence
import utils.brMoney
import view.shared.TextNormal
import view.shared.TextSmall
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs

@Composable
fun UpcomingSchedulesCard(
    modifier: Modifier = Modifier,
    occurrences: List<ScheduleOccurrence>,
) {
    SummaryCard(
        modifier = modifier,
        title = "Lançamentos futuros",
        icon = PhosphorIcons.Light.CalendarBlank,
        height = 240.dp,
    ) {
        if (occurrences.isEmpty()) {
            TextSmall(text = "Nenhum lançamento futuro", italic = true)
            return@SummaryCard
        }

        val ptBr = Locale.of("pt", "BR")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            occurrences.forEach { occurrence ->
                val schedule = occurrence.schedule
                val dueDate = occurrence.dueDate

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                when (schedule.type) {
                                    TransactionType.GAIN -> MaterialTheme.colors.onPrimary
                                    TransactionType.EXPENSE -> MaterialTheme.colors.onError
                                    else -> MaterialTheme.colors.primaryVariant
                                }
                            )
                            .size(8.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        TextNormal(text = schedule.party.name)
                        Spacer(Modifier.height(2.dp))
                        val day = dueDate.dayOfMonth
                        val month = dueDate.month.getDisplayName(TextStyle.SHORT, ptBr)
                        TextSmall(text = "$day $month · ${schedule.category.name}")
                    }
                    val balanceColor = when (schedule.type) {
                        TransactionType.GAIN -> MaterialTheme.colors.onPrimary
                        TransactionType.EXPENSE -> MaterialTheme.colors.onError
                        else -> MaterialTheme.colors.onSurface
                    }
                    TextNormal(text = brMoney.format(abs(schedule.balance)), color = balanceColor)
                }
            }
        }
    }
}
