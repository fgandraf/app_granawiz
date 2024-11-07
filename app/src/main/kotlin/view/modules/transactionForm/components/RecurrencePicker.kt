package view.modules.transactionForm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import domain.enums.ScheduleFrequency
import viewModel.TransactionFormViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun buildRecurrenceSummary(
    frequency: ScheduleFrequency,
    interval: Int,
    installments: Int?,
    endDate: LocalDateTime?,
): String {
    val freqPart = when {
        frequency == ScheduleFrequency.ONCE -> stringResource(Res.string.recurrence_once)
        frequency == ScheduleFrequency.DAILY && interval == 1 -> stringResource(Res.string.recurrence_daily)
        frequency == ScheduleFrequency.DAILY -> stringResource(Res.string.recurrence_every_n_days, interval)
        frequency == ScheduleFrequency.WEEKLY && interval == 1 -> stringResource(Res.string.recurrence_weekly)
        frequency == ScheduleFrequency.WEEKLY -> stringResource(Res.string.recurrence_every_n_weeks, interval)
        frequency == ScheduleFrequency.MONTHLY && interval == 1 -> stringResource(Res.string.recurrence_monthly)
        frequency == ScheduleFrequency.MONTHLY -> stringResource(Res.string.recurrence_every_n_months, interval)
        frequency == ScheduleFrequency.YEARLY && interval == 1 -> stringResource(Res.string.recurrence_yearly)
        else -> stringResource(Res.string.recurrence_every_n_years, interval)
    }
    val terminationPart = when {
        installments != null -> " · $installments ${stringResource(Res.string.recurrence_installments_suffix)}"
        endDate != null -> " · ${stringResource(Res.string.recurrence_summary_until_date, endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))}"
        else -> ""
    }
    return freqPart + terminationPart
}

@Composable
fun RecurrencePicker(viewModel: TransactionFormViewModel) {
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
