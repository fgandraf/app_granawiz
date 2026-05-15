package view.modules.schedules.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Check
import com.adamglin.phosphoricons.light.DotsThree
import com.adamglin.phosphoricons.light.Pen
import com.adamglin.phosphoricons.light.Repeat
import com.adamglin.phosphoricons.light.Tag
import com.adamglin.phosphoricons.light.Trash
import domain.enums.ScheduleFrequency
import domain.enums.TransactionType
import application.schedule.usecases.ScheduleOccurrence
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import utils.IconPaths
import utils.formatNumber
import utils.rememberSvgPainter
import view.shared.*
import viewModel.ScheduleViewModel
import java.time.format.TextStyle
import java.util.*

@Composable
fun ScheduleRow(
    viewModel: ScheduleViewModel,
    occurrence: ScheduleOccurrence,
    overdue: Boolean,
    onEdit: () -> Unit,
    onSidebarReload: () -> Unit = {},
) {
    val schedule = occurrence.schedule
    val dueDate = occurrence.dueDate

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(0.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onEdit() }
    ) {

        // Type dot
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().padding(start = 30.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (schedule.type == TransactionType.GAIN) MaterialTheme.colors.onPrimary
                        else if (schedule.type == TransactionType.EXPENSE) MaterialTheme.colors.onError
                        else MaterialTheme.colors.primaryVariant
                    )
                    .size(10.dp)
            )
        }

        // Party + due date
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight().weight(0.9f).padding(end = 10.dp)
        ) {
            TextNormal(
                modifier = Modifier.padding(bottom = 2.dp),
                text = schedule.party.name
            )
            val day = dueDate.dayOfMonth
            val month = dueDate.month.getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"))
            val color = if (overdue) MaterialTheme.colors.onError else MaterialTheme.colors.primary
            TextSmall(text = stringResource(Res.string.schedule_due_date, day.toString(), month), color = color)
        }

        // Category + subcategory
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().weight(0.9f).padding(end = 10.dp)
        ) {
            if (schedule.category.icon.isNotBlank()) {
                Icon(
                    painter = rememberSvgPainter(IconPaths.CATEGORY_PACK + schedule.category.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(15.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
            TextNormal(text = schedule.category.name)
            if (schedule.subcategory != null) {
                Spacer(Modifier.width(5.dp))
                TextNormal(text = "→")
                Spacer(Modifier.width(5.dp))
                TextNormal(text = schedule.subcategory!!.name)
            }
        }

        // Frequency badge + tags
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().weight(0.9f).padding(end = 10.dp)
        ) {
            Icon(
                imageVector = PhosphorIcons.Light.Repeat,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(5.dp))
            TextSmall(text = frequencyLabel(schedule.frequency, schedule.interval))
            if (!schedule.tags.isNullOrEmpty()) {
                Spacer(Modifier.width(15.dp))
                schedule.tags?.forEach { tag ->
                    Row {
                        Icon(
                            imageVector = PhosphorIcons.Light.Tag,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        TextNormal(text = tag.name)
                        Spacer(Modifier.width(15.dp))
                    }
                }
            }
        }

        // Installment (if schedule has installments)
        if (schedule.installments != null) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight().padding(end = 10.dp).weight(0.3f)
            ) {
                TextSmall(text = "${occurrence.index + 1}/${schedule.installments}")
            }
        }

        // Balance
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().padding(end = 10.dp).weight(0.4f)
        ) {
            TextNormal(text = formatNumber(schedule.balance))
        }

        // Mark as paid
        var confirmMarkAsPaid by remember { mutableStateOf(false) }
        if (confirmMarkAsPaid) {
            SimpleQuestionDialog(
                message = stringResource(Res.string.schedule_confirm_payment),
                onConfirmRequest = {
                    viewModel.markAsPaid(occurrence)
                    onSidebarReload()
                    confirmMarkAsPaid = false
                },
                onDismissRequest = { confirmMarkAsPaid = false },
            )
        }

        Row(
            Modifier.fillMaxHeight().padding(end = 8.dp),
            Arrangement.End,
            Alignment.CenterVertically
        ) {
            @OptIn(ExperimentalFoundationApi::class)
            TooltipArea(
                tooltip = {
                    Surface(
                        modifier = Modifier.shadow(4.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colors.surface,
                    ) {
                        TextSmall(
                            text = stringResource(Res.string.mark_as_paid),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        )
                    }
                },
                tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colors.onPrimary, CircleShape)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clickable { confirmMarkAsPaid = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = PhosphorIcons.Light.Check,
                        contentDescription = stringResource(Res.string.mark_as_paid),
                        tint = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // More menu
        var showMenu by remember { mutableStateOf(false) }
        Row(Modifier.fillMaxHeight().padding(end = 20.dp), Arrangement.End, Alignment.CenterVertically) {
            ClickableIcon(
                icon = PhosphorIcons.Light.DotsThree,
                shape = RoundedCornerShape(6.dp),
                onClick = { showMenu = true },
            )

            if (showMenu) {
                DropdownMenu(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    var triggerEdit by remember { mutableStateOf(false) }
                    ClickableRow(icon = PhosphorIcons.Light.Pen, label = stringResource(Res.string.edit)) {
                        triggerEdit = true
                    }
                    if (triggerEdit) {
                        onEdit()
                        showMenu = false
                    }

                    var confirmDelete by remember { mutableStateOf(false) }
                    ClickableRow(icon = PhosphorIcons.Light.Trash, label = stringResource(Res.string.delete)) {
                        confirmDelete = true
                    }
                    if (confirmDelete) {
                        DeleteScheduleDialog(
                            onDismiss = { confirmDelete = false; showMenu = false },
                            onDeleteThis = {
                                viewModel.deleteThisOccurrence(occurrence)
                                confirmDelete = false
                                showMenu = false
                            },
                            onDeleteThisAndFuture = {
                                viewModel.deleteThisAndFuture(occurrence)
                                confirmDelete = false
                                showMenu = false
                            },
                        )
                    }
                }
            }
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
