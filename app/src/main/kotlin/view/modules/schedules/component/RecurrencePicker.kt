package view.modules.schedules.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.enums.ScheduleFrequency
import viewModel.ScheduleFormViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun buildRecurrenceSummary(
    frequency: ScheduleFrequency,
    interval: Int,
    installments: Int?,
    endDate: LocalDateTime?,
): String {
    val freqPart = when {
        frequency == ScheduleFrequency.ONCE -> "Uma única vez"
        frequency == ScheduleFrequency.DAILY && interval == 1 -> "Diária"
        frequency == ScheduleFrequency.DAILY -> "A cada $interval dias"
        frequency == ScheduleFrequency.WEEKLY && interval == 1 -> "Semanal"
        frequency == ScheduleFrequency.WEEKLY -> "A cada $interval semanas"
        frequency == ScheduleFrequency.MONTHLY && interval == 1 -> "Mensal"
        frequency == ScheduleFrequency.MONTHLY -> "A cada $interval meses"
        frequency == ScheduleFrequency.YEARLY && interval == 1 -> "Anual"
        else -> "A cada $interval anos"
    }
    val terminationPart = when {
        installments != null -> " · $installments parcela(s)"
        endDate != null -> " · até ${endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
        else -> ""
    }
    return freqPart + terminationPart
}

@Composable
fun RecurrencePicker(viewModel: ScheduleFormViewModel) {
    val corner = 10.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(750.dp)
            .padding(top = 50.dp)
            .background(MaterialTheme.colors.surface, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
            .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 30.dp, horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            RecurrenceSection(viewModel)
        }
    }
}
