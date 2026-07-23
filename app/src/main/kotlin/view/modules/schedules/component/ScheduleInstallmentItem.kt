package view.modules.schedules.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Check
import com.adamglin.phosphoricons.light.Pen
import com.adamglin.phosphoricons.light.Trash
import application.schedule.usecases.ScheduleOccurrence
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import utils.formatNumber
import view.shared.ClickableIcon
import view.shared.SimpleQuestionDialog
import view.shared.TextSmall
import view.shared.TooltipBox
import java.time.format.TextStyle
import java.util.*

@Composable
fun ScheduleInstallmentItem(
    occurrence: ScheduleOccurrence,
    overdue: Boolean,
    onEdit: () -> Unit,
    onMarkAsPaid: () -> Unit,
    onDelete: () -> Unit,
) {
    val schedule = occurrence.schedule
    val dueDate = occurrence.dueDate

    var confirmMarkAsPaid by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    if (confirmMarkAsPaid) {
        SimpleQuestionDialog(
            message = stringResource(Res.string.schedule_confirm_payment),
            onConfirmRequest = { onMarkAsPaid(); confirmMarkAsPaid = false },
            onDismissRequest = { confirmMarkAsPaid = false },
        )
    }

    if (confirmDelete) {
        SimpleQuestionDialog(
            message = stringResource(Res.string.schedule_delete_installment_warning),
            onConfirmRequest = { onDelete(); confirmDelete = false },
            onDismissRequest = { confirmDelete = false },
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().height(38.dp).padding(horizontal = 15.dp)
    ) {
        if (schedule.installments != null) {
            TextSmall(
                text = "${occurrence.index + 1}/${schedule.installments}",
                modifier = Modifier.width(45.dp)
            )
        }

        val day = dueDate.dayOfMonth
        val month = dueDate.month.getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"))
        val dueColor = if (overdue) MaterialTheme.colors.onError else MaterialTheme.colors.primary
        TextSmall(
            text = stringResource(Res.string.schedule_due_date, day.toString(), month),
            color = dueColor,
            modifier = Modifier.weight(1f)
        )

        TextSmall(text = formatNumber(schedule.balance), modifier = Modifier.padding(end = 10.dp))

        TooltipBox(label = stringResource(Res.string.mark_as_paid)) {
            ClickableIcon(
                icon = PhosphorIcons.Light.Check,
                boxSize = 26.dp,
                iconSize = 14.dp,
                onClick = { confirmMarkAsPaid = true },
            )
        }
        TooltipBox(label = stringResource(Res.string.edit)) {
            ClickableIcon(
                icon = PhosphorIcons.Light.Pen,
                boxSize = 26.dp,
                iconSize = 14.dp,
                onClick = onEdit,
            )
        }
        TooltipBox(label = stringResource(Res.string.delete)) {
            ClickableIcon(
                icon = PhosphorIcons.Light.Trash,
                boxSize = 26.dp,
                iconSize = 14.dp,
                onClick = { confirmDelete = true },
            )
        }
    }
}
