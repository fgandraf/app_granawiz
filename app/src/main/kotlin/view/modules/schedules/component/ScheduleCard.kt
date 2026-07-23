package view.modules.schedules.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Calendar
import com.adamglin.phosphoricons.light.CaretDown
import com.adamglin.phosphoricons.light.CaretRight
import com.adamglin.phosphoricons.light.DotsThree
import com.adamglin.phosphoricons.light.Pen
import com.adamglin.phosphoricons.light.Repeat
import com.adamglin.phosphoricons.light.Tag
import com.adamglin.phosphoricons.light.Trash
import application.schedule.usecases.ScheduleOccurrence
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import domain.entity.Schedule
import domain.enums.ScheduleFrequency
import domain.enums.TransactionType
import org.jetbrains.compose.resources.stringResource
import utils.IconPaths
import utils.formatCurrency
import utils.rememberAccountIconPainter
import utils.rememberSvgPainter
import view.shared.ClickableIcon
import view.shared.ClickableRow
import view.shared.SimpleQuestionDialog
import view.shared.TextH3
import view.shared.TextNormal
import view.shared.TextSmall
import view.shared.TooltipBox
import viewModel.UserPreferences
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ScheduleCard(
    modifier: Modifier = Modifier,
    schedule: Schedule,
    occurrences: List<ScheduleOccurrence>,
    highlightColor: Color? = null,
    onEdit: (ScheduleOccurrence) -> Unit,
    onEditSeries: (Schedule) -> Unit,
    onMarkAsPaid: (ScheduleOccurrence) -> Unit,
    onDeleteOccurrence: (ScheduleOccurrence) -> Unit,
    onDeleteSeries: (Schedule) -> Unit,
) {
    if (occurrences.isEmpty()) return

    var expanded by remember { mutableStateOf(false) }
    var confirmDeleteSeries by remember { mutableStateOf(false) }

    val title = schedule.description.substringBefore("\n").trim().ifBlank { schedule.party.name }

    if (confirmDeleteSeries) {
        SimpleQuestionDialog(
            message = stringResource(Res.string.schedule_delete_series_warning),
            onConfirmRequest = { onDeleteSeries(schedule); confirmDeleteSeries = false },
            onDismissRequest = { confirmDeleteSeries = false },
        )
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.background.copy(0.6f))
            .border(
                if (highlightColor != null) 1.5.dp else 0.5.dp,
                highlightColor ?: MaterialTheme.colors.onSurface,
                RoundedCornerShape(8.dp)
            )
    ) {
        // Header: centered title with the 3-dots menu near the right border
        var showMenu by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(44.dp).padding(start = 10.dp, end = 4.dp)
        ) {
            TooltipBox(label = schedule.account.name) {
                Icon(
                    painter = rememberAccountIconPainter(schedule.account.icon, schedule.account.iconSvg),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(18.dp)
                )
            }

            TextH3(
                text = title,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                align = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Box {
                ClickableIcon(
                    icon = PhosphorIcons.Light.DotsThree,
                    shape = RoundedCornerShape(6.dp),
                    onClick = { showMenu = true },
                )
                DropdownMenu(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    ClickableRow(icon = PhosphorIcons.Light.Pen, label = stringResource(Res.string.edit)) {
                        showMenu = false
                        onEditSeries(schedule)
                    }
                    ClickableRow(icon = PhosphorIcons.Light.Trash, label = stringResource(Res.string.delete)) {
                        showMenu = false
                        confirmDeleteSeries = true
                    }
                }
            }
        }

        Divider(color = MaterialTheme.colors.onSurface.copy(0.4f), thickness = 0.5.dp)

        // Body: 3 equally-spaced lines — category, recurrence, pending installments
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Line 0: next due date
            Row(verticalAlignment = Alignment.CenterVertically) {
                val nextDue = occurrences.first().dueDate
                Icon(
                    imageVector = PhosphorIcons.Light.Calendar,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(8.dp))
                val day = nextDue.dayOfMonth
                val month = nextDue.month.getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"))
                TextSmall(text = stringResource(Res.string.schedule_due_date, day.toString(), month))
            }

            // Line 1: category / subcategory
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (schedule.category.icon.isNotBlank()) {
                    Icon(
                        painter = rememberSvgPainter(IconPaths.CATEGORY_PACK + schedule.category.icon),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                TextSmall(text = schedule.category.name)
                if (schedule.subcategory != null) {
                    Spacer(Modifier.width(5.dp))
                    TextSmall(text = "→")
                    Spacer(Modifier.width(5.dp))
                    TextSmall(text = schedule.subcategory!!.name)
                }
            }

            // Line 2: recurrence + installments count
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = PhosphorIcons.Light.Repeat,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(5.dp))
                TextSmall(text = frequencyLabel(schedule.frequency, schedule.interval))
                Spacer(Modifier.width(14.dp))
                if (schedule.installments != null) {
                    TextSmall(
                        text = stringResource(
                            Res.string.schedule_installments_count,
                            occurrences.size,
                            schedule.installments!!
                        )
                    )
                } else {
                    TextSmall(text = stringResource(Res.string.schedule_recurring))
                }
            }

            // Tags
            if (!schedule.tags.isNullOrEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    schedule.tags?.forEach { tag ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = PhosphorIcons.Light.Tag,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            TextSmall(text = tag.name)
                        }
                    }
                }
            }

            // Line 3: collapsible pending installments header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable { expanded = !expanded }
            ) {
                Icon(
                    imageVector = if (expanded) PhosphorIcons.Light.CaretDown else PhosphorIcons.Light.CaretRight,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(8.dp))
                TextSmall(
                    text = "${stringResource(Res.string.schedule_pending_installments)} (${occurrences.size})"
                )
            }
        }

        if (expanded) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                occurrences.forEach { occ ->
                    ScheduleInstallmentItem(
                        occurrence = occ,
                        overdue = occ.dueDate.isBefore(java.time.LocalDate.now().atStartOfDay()),
                        onEdit = { onEdit(occ) },
                        onMarkAsPaid = { onMarkAsPaid(occ) },
                        onDelete = { onDeleteOccurrence(occ) },
                    )
                }
            }
        }

        Divider(color = MaterialTheme.colors.onSurface.copy(0.4f), thickness = 0.5.dp)

        // Footer: centered value with currency symbol
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().height(44.dp).padding(horizontal = 15.dp)
        ) {
            TextNormal(
                text = formatCurrency(schedule.balance, UserPreferences.currencySymbol),
                color = when (schedule.type) {
                    TransactionType.EXPENSE -> MaterialTheme.colors.onError
                    TransactionType.GAIN -> MaterialTheme.colors.onPrimary
                    else -> MaterialTheme.colors.primary
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun frequencyLabel(frequency: ScheduleFrequency, interval: Int): String = when (frequency) {
    ScheduleFrequency.ONCE -> stringResource(Res.string.recurrence_once)
    ScheduleFrequency.DAILY -> if (interval == 1) stringResource(Res.string.recurrence_daily)
                               else stringResource(Res.string.recurrence_every_n_days, interval)
    ScheduleFrequency.WEEKLY -> if (interval == 1) stringResource(Res.string.recurrence_weekly)
                                else stringResource(Res.string.recurrence_every_n_weeks, interval)
    ScheduleFrequency.MONTHLY -> if (interval == 1) stringResource(Res.string.recurrence_monthly)
                                 else stringResource(Res.string.recurrence_every_n_months, interval)
    ScheduleFrequency.YEARLY -> if (interval == 1) stringResource(Res.string.recurrence_yearly)
                                else stringResource(Res.string.recurrence_every_n_years, interval)
}
